/*
 * Copyright 2014 Renaud Bruneliere
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.bruneli.scalaopt.stdapps

import com.github.bruneli.scalaopt.core._
import com.github.bruneli.scalaopt.core.function._
import com.github.bruneli.scalaopt.core.leastsquares.{LevenbergMarquardt, LevenbergMarquardtConfig}
import com.github.bruneli.scalaopt.core.variable.UnconstrainedVariable
import com.github.bruneli.scalaopt.core.derivativefree.NelderMead

/**
 * Statistics package using optimization algorithms to fit and predict set of data.
 *
 * @author bruneli
 */
package object stats {

  /**
   * Use result from a fit to predict y values for new data
   *
   * @param fit     results from a least squares or maximum likelihood fit
   * @param newData new data points X
   * @return set of points (X, y) with y being estimated from fit
   */
  def predict(fit: FitResults, newData: DataSet[InputsType]) = {
    newData.map(x => (x, fit.predict(x)))
  }

  def predict(fit: FitResults, newData: Seq[Double]) = {
    newData.map(x => (x, fit.predict(x)))
  }

  /**
   * Fit a function with p parameters to data points (X, y) using the least squares method.
   *
   * @param func   objective function taking as input parameters p and real data observations X
   * @param p0     initial parameter values
   * @param config optimization method configuration parameters
   * @return fit results
   */
  def leastSquaresFit[C <: LevenbergMarquardtConfig](
    func: SimpleMSEFunction,
    p0: UnconstrainedVariablesType,
    config: Option[C] = None): FitResults = {
    implicit val pars = config.getOrElse(LevenbergMarquardt.defaultConfig)
    val pOpt = LevenbergMarquardt.minimize(func, p0).get
    new FitResults(pOpt, func.f)
  }

  /**
   * Fit a probability density function with p parameters to data points X using the maximum likelihood principle.
   *
   * @param pdf    probability density function taking as input parameters p and real data observations X
   * @param data   a set of points X
   * @param p0     initial real parameter values
   * @param method an optimization method
   * @param config optimization method configuration parameters
   * @return fit results
   */
  def maxLikelihoodFit[A <: ContinuousObjectiveFunction[UnconstrainedVariable], B <: ConfigPars](
    pdf: RegressionFunction,
    data: DataSet[InputsType],
    p0: UnconstrainedVariablesType,
    method: Optimizer[UnconstrainedVariable, A, B] = NelderMead,
    config: Option[B] = None): FitResults = {
    implicit val pars = config.getOrElse(method.defaultConfig)
    val nllf = negLogLikelihood(pdf, data)_ match {
      case f: A => f
      case _ => throw new ClassCastException("Incorrect type of negative log-likelihood function")
    }
    val pOpt = method.minimize(nllf, p0).get
    new FitResults(pOpt, pdf)
  }

  /**
   * Compute twice the negative log likelihood of pdf with data points X
   *
   * @param pdf  probability density function taking as input parameters p and real data observations X
   * @param data a set of points X
   * @param p    real parameter values
   * @return -2 times the log likelihood
   */
  def negLogLikelihood(
    pdf: RegressionFunction,
    data: DataSet[InputsType])(
    p: UnconstrainedVariablesType): Double = {
    val value = data.aggregate(0.0)((sum, x) => sum - 2.0 * Math.log(pdf(p, x)(0)), _ + _)
    value
  }

}
