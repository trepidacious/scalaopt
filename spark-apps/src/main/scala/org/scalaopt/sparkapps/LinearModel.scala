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

package org.scalaopt.sparkapps

import org.apache.spark.SparkContext
import org.scalaopt.algos.Coordinates

/**
 * @author bruneli
 */
object LinearModel {

  def nllr(mu: Double)(n: Int) = mu - n*Math.log(mu)

  def main(args: Array[String]) {
/*
    val sc = new SparkContext()
    val rdd = sc.parallelize(1 to 10)
*/
    println(s"Solving a linear model via QR decomposition.")
  }
}
