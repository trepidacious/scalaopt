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

package org.scalaopt.algos.gradient

import org.scalaopt.algos._
import scala.util.{Try, Success, Failure}
import org.scalatest._
import org.scalatest.Matchers._

class BFGSSpec extends FlatSpec with Matchers {
  import BFGS._
  
  val x0 = Vector(0.5, 2.0)
  def fQuad(x: Coordinates): Double = 
    (x - x0) dot (x - x0)
  def dfQuad(x: Coordinates): Coordinates = 
    (x - x0) * 2.0

  val config = new BFGSConfig(tol = 1.0e-6)

  "minimize with exact derivatives" should "converge to x0" in {
    val d = minimize(fQuad, dfQuad, Vector(0.0, 0.0)) match {
      case Success(xmin) => xmin - x0
      case Failure(e) => x0
    }
    (d dot d) should be < (config.tol * config.tol)
  }

  "minimize with finite diff derivatives" should "converge to x0" in {
    val d = minimize(fQuad, Vector(0.0, 0.0)) match {
      case Success(xmin) => xmin - x0
      case Failure(e) => x0
    }
    (d dot d) should be < (config.tol * config.tol)
  }
  
  "minimize" should "throw an error if reaching max number of iterations" in {
    a [MaxIterException] should be thrownBy {
      minimize(x => x(0) + x(1), Vector(0.0, 0.0))
    }
  }
  
  it should "throw an error if wrong configuration type" in {
    import ConjugateGradient.CGConfig
    val wrongc = new CGConfig
    a [IllegalArgumentException] should be thrownBy {
      minimize(x => x(0) + x(1), Vector(0.0, 0.0))(wrongc)
    }
  }
}