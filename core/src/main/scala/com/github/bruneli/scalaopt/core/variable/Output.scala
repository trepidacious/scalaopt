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

package com.github.bruneli.scalaopt.core.variable

import com.github.bruneli.scalaopt.core.RealVectorType
import com.github.bruneli.scalaopt.core.linalg.FromToDoubleConversions.ToDouble
import com.github.bruneli.scalaopt.core.linalg.SimpleDenseVector

/**
 * Response/Dependent/Output variable for a regression model
 *
 * @author bruneli
 */
case class Output(x: Double) extends AnyVal with ToDouble

/**
 * Vector of output targets
 */
class Outputs(raw: Array[Double])
  extends SimpleDenseVector[Output](raw)(ConversionsFromDouble.OutputFromDouble)

object Outputs {

  def apply(outputs: Double*): Outputs = {
    new Outputs(outputs.toArray)
  }

  def apply(vector: RealVectorType): Outputs = {
    val coordinates = new Array[Double](vector.length)
    for (i <- coordinates.indices) {
      coordinates(i) = vector.coordinate(i)
    }
    new Outputs(coordinates)
  }

}
