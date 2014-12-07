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

package org.scalaopt.algos.linalg

import org.scalaopt.algos
import org.scalaopt.algos._

/**
 * Row to represent an augmented matrix used to solve a linear system AX = B
 *
 * @param a a row in the matrix
 * @param b solution for that row
 * @param i index of the row
 *
 * @author bruneli
 */
case class AugmentedRow(a: Coordinates, b: Double, i: Long) {

  def +(that: AugmentedRow): AugmentedRow =
    AugmentedRow(that.a + this.a, that.b + this.b, i)

  override def toString = s"row $i (${a.mkString(", ")} | $b)"

}

object AugmentedRow {

  def zeros(n: Int): AugmentedRow =
    AugmentedRow(algos.zeros(n), 0.0, 0)

}