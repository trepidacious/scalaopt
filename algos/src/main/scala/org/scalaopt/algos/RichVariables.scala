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

import org.apache.commons.math3.linear.{RealMatrix, MatrixUtils}

/**
 * Enrich Seq[Double] with mathematical operations
 * on vectors in the Euclidean space.
 * 
 * @param v a sequence of Double values treated as Variables
 * 
 * @author bruneli
 */
class RichVariables(v: Variables) {

  /** Element wise addition */
  def + (that: Variables): Variables =
    this.v.zip(that).map { case (x, y) => x + y }
  
  /** Element wise subtraction */
  def - (that: Variables): Variables =
    this.v.zip(that).map { case (x, y) => x - y }

  /** Negative of a vector */
  def unary_- : Variables = this.v.map(-1.0 * _)
  
  /** Multiplication by scalar */
  def * (scalar: Double): Variables =
    this.v.map(_ * scalar)
  
  /** Division by scalar */
  def / (scalar: Double): Variables =
    if (scalar == 0.0)
      throw new IllegalArgumentException("scalar should be != 0.0")
    else
      this.v.map(_ / scalar)
  
  /** Inner product of two vectors */
  def inner(that: Variables): Double =
    if (that.length == this.v.length) {
      this.v.zip(that).foldLeft(0.0) { case (r, c) => r + c._1 * c._2 }
    } else {
      throw new IllegalArgumentException(
          "The two vectors should have same dimensions.")
    }
      
  /** Dot product of two vectors */
  def dot(that: Variables): Double = this inner that

  /** L2 norm */
  def norm: Double = math.sqrt(norm2)
  
  /** L2 norm squared */
  def norm2: Double = this.v.map(x => x * x).sum
  
  /** Conversion to a column matrix */
  def toMatrix: RealMatrix = MatrixUtils.createColumnRealMatrix(v.toArray)
  
  /** Transpose, conversion to a row matrix */
  def t: RealMatrix = this.toMatrix.transpose()

  /** Outer product of two vectors */
  def outer(that: RichVariables): RealMatrix =
    this.toMatrix * that.t

}