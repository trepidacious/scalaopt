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

package com.github.bruneli.scalaopt.core.derivativefree

import com.github.bruneli.scalaopt.core._
import com.github.bruneli.scalaopt.core.variable.UnconstrainedVariables

import scala.util.{Failure, Success}
import org.scalatest._

class PowellSpec extends FlatSpec with Matchers {
  import Powell._
  
  val x0 = UnconstrainedVariables(0.5, 2.0)
  val fQuad = (x: UnconstrainedVariablesType) => (x - x0) dot (x - x0)

  val config = new PowellConfig(tol = 1.0e-6)
    
  "minimize" should "be close to x0" in {
    val d = minimize(fQuad, UnconstrainedVariables(0.0, 0.0)) match {
      case Success(xmin) => xmin - x0
      case Failure(e) => x0
    }
    (d dot d) should be < (config.tol * config.tol)
  }

  it should "throw an error if reaching max number of iterations" in {
    a [MaxIterException] should be thrownBy {
      minimize((x: UnconstrainedVariablesType) => x(0) + x(1), UnconstrainedVariables(0.0, 0.0))
    }
  }

}