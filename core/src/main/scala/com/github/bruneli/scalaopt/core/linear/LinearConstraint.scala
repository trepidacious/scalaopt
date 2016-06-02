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

package com.github.bruneli.scalaopt.core.linear

import com.github.bruneli.scalaopt.core._

import scala.util.Try
import ConstraintOperator._
import SeqDataSetConverter._

/**
 * Define a general linear constraint as ax = b, ax <= b or ax >= b with a an n-dimensional real valued vector
 *
 * @param a   n-dimension real-valued vector
 * @param op  equality/inequality operator
 * @param b   right hand side value
 * @param eps precision to check a strict equality
 * @author bruneli
 */
case class LinearConstraint(
  a: DataSet[Double],
  op: ConstraintOperator,
  b: Double,
  eps: Double = 1.0e-8) {

  /**
   * Check if constraint is satisfied in x
   *
   * @param x real-valued vector
   * @return true if constraint is satisfied in x
   */
  def apply(x: Variables): Boolean = op match {
    case EQ => Math.abs((a.collect() dot x) - b) <= eps
    case LE => (a.collect() dot x) <= b
    case GE => (a.collect() dot x) >= b
  }

  /**
   * Convert a linear constraint to a tableau column
   */
  def toColumn(column: Long): TableauColumn = {
    TableauColumn(0.0, b, a.collect().toVector, column)
  }

  /**
   * Resize a constraint to be of size n
   *
   * @param n new size of the constraint
   * @return constraint with an updated size n
   * @throws IllegalArgumentException when new size n is smaller than initial size
   */
  def resize(n: Int): LinearConstraint = {
    val m = this.a.size.toInt
    require(n >= m,
      s"Size of the new linear constraint $n should be >= initial constraint size $m")
    if (n == m) {
      this
    } else {
      this.copy(a = this.a ++ zeros(n - m))
    }
  }

  /**
   * Convert a linear constraint into a general constraint class
   */
  def toConstraint: Constraint = {
    Constraint((x: Variables) => a.collect() dot x, op, b)
  }

  /**
   * Transform a general linear constraint into an n-dimensional linear equality constraint
   *
   * The initial constraint must have a number of dimensions smaller or equal to the new number of dimensions
   *
   * @param n     number of dimensions of the new constraint left hand side
   * @param slack position of the slack variable (optional, but mandatory for an initial inequality constraint)
   * @return an n-dimensional linear equality constraint
   */
  def toEquality(n: Int, slack: Option[Int] = None): LinearConstraint = {
    val m = this.a.size.toInt
    require(n > m || op == EQ && n == m,
      s"Size of the new linear constraint $n should be >= initial constraint size $m")
    require(op == EQ || slack.isDefined, s"Every inequality constraint must specify a slack variable position")
    require(slack.isEmpty || slack.get >= m && slack.get < n,
      s"Slack variable position ${slack.get} must be >= $m and < $n")
    val a = op match {
      case EQ => if (n == m) this.a else this.a ++ zeros(n - m)
      case LE =>
        // Add a slack variable to have equality satisfied
        val s = slack.get - m
        this.a ++ e(n - m, s)
      case GE =>
        // Add an excess variable to have equality satisfied
        val s = slack.get - m
        this.a ++ (e(n - m, s) * -1.0)
    }
    LinearConstraint(a, EQ, b)
  }

  /**
   * Produce a new linear constraint with a positive right-hand-side value
   */
  def withPositiveRhs: LinearConstraint = {
    if (b < 0.0) {
      val inverseOp = op match {
        case LE => GE
        case EQ => EQ
        case GE => LE
      }
      LinearConstraint(a.map(_ * -1.0), inverseOp, -b)
    } else {
      this
    }
  }

}

object LinearConstraint {

  /**
   * Try to build an n-dimensional linear constraint from a general constraint
   *
   * @param constraint general constraint function
   * @param n          number of dimensions of the left hand side of the constraint
   * @return linear constraint expressed as a left hand side vector, an equality operator and a right hand side value
   */
  def apply(constraint: Constraint, n: Int): Try[LinearConstraint] =
    Try(constraint.c(ones(n))).map {
      value =>
      val a = (0 until n).map(i => constraint.c(e(n, i)))
      LinearConstraint(a, constraint.op, constraint.b)
    }

}
