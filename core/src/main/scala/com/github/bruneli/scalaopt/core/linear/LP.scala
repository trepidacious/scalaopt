/*
 * Copyright 2016 Renaud Bruneliere
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

package com.github.bruneli.scalaopt.core.linear

import com.github.bruneli.scalaopt.core.constraint.{CP, CPBuilder, CPSolver, LinearConstraint}
import com.github.bruneli.scalaopt.core.function.LinearContinuousObjectiveFunction
import com.github.bruneli.scalaopt.core.variable.ContinuousVariable

/**
 * Define a linear program as a continuous linear objective function and a set of
 * continuous linear constraints.
 *
 * @author bruneli
 */
trait LP extends CP[ContinuousVariable, LinearContinuousObjectiveFunction[ContinuousVariable], LinearConstraint[ContinuousVariable]] {

  def toTableau: SimplexTableau

}