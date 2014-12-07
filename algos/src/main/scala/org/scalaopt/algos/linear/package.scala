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

package org.scalaopt.algos

import org.scalaopt.algos.linalg.{AugmentedRow, QR, DataSet}

import scala.util.Try

/**
 * @author bruneli
 */
package object linear {

  def lm(f: ObjFunWithData,
         data: DataSet[Xy]): Try[Coordinates] =
    Try {
      val n = data.head._1.size
      val ab = data.zipWithIndex.map {
        case (row, index) => AugmentedRow(row._1, row._2, index)
      }
      QR(ab, n).solution
    }

}