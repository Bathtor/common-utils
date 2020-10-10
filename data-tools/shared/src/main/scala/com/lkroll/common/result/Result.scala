/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Lars Kroll <bathtor@googlemail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.lkroll.common.result

import scala.util.{Failure, Success, Try}

/**
  * A [[https://doc.rust-lang.org/std/result/index.html Rust-style]] `Result` type for error handling.
  *
  * It works essentially like Scala's `Either`, just with sensible naming,
  * and the "success" type argument coming first, as one would expect.
  *
  * The two possible variants are called [[com.lkroll.common.result.Ok Ok]]
  * and [[com.lkroll.common.result.Err Err]].
  *
  *  For example, you could use `Result[Int, String]` to indicate whether a
  *  received input is an `Int`, as expected, or fall back to reproducing the
  *  input as a `String` in the error case.
  *
  *  {{{
  *  import scala.io.StdIn._
  *  val in = readLine("Type an Int: ")
  *  val result: Result[Int, String] =
  *    try Ok(in.toInt)
  *    catch {
  *       case e: NumberFormatException => Err(in)
  *    }
  *
  *  result match {
  *    case Ok(x) => s"You passed me the Int: $x, which I will increment. $x + 1 = ${x+1}"
  *    case Err(x)  => s"You passed me the String: $x"
  *  }
  *  }}}
  */
sealed abstract class Result[+V, +E] extends Product with Serializable {

  /**
    * Checks whether or not this is an instance of [[com.lkroll.common.result.Ok Ok]].
    */
  def isOk: Boolean;

  /**
    * Checks whether or not this is an instance of [[com.lkroll.common.result.Err Err]].
    */
  def isErr: Boolean;

  /**
    * Converts from `Result[V, E]` to `Option[V]`.
    *
    * Discards the error value, if any.
    */
  def ok(): Option[V] = this match {
    case Ok(v)  => Some(v)
    case Err(_) => None
  }

  /**
    * Converts from `Result[V, E]` to `Option[V]`.
    *
    * Discards the error value, if any.
    *
    * Same as [[com.lkroll.common.result.Result#ok ok()]].
    */
  def toOption: Option[V] = ok();

  /**
    * Converts from `Result[V, E]` to `Option[E].
    *
    * Discards the success value, if any.
    */
  def err(): Option[E] = this match {
    case Ok(_)  => None
    case Err(e) => Some(e)
  }

  /**
    * Converts from `Result[V, E]` to `Try[V]`.
    *
    * If `E <: Throwable` and `this` is [[com.lkroll.common.result.Err Err]]
    * then it produces a `Failure[E]`.
    * For other `E` it produces the same output as `Try(this.get)` would.
    */
  def toTry: Try[V] = this match {
    case Ok(v)             => Success(v)
    case Err(e: Throwable) => Failure(e)
    case Err(e)            => Try(this.get)
  }

  /**
    * Maps a `Result[V,E]` to `Result[T,E]`
    * by applying a function to a contained [[com.lkroll.common.result.Ok Ok]] value,
    * leaving an [[com.lkroll.common.result.Err Err]] value untouched.
    *
    * This function can be used to compose the results of two functions.
    */
  def map[T](f: V => T): Result[T, E] = this match {
    case Ok(v)  => Ok(f(v))
    case Err(e) => Err(e)
  }

  /**
    * Maps a `Result[V,E]` to `Result[V,F]`
    * by applying a function to a contained [[com.lkroll.common.result.Err Err]] value,
    * leaving an [[com.lkroll.common.result.Ok Ok]] value untouched.
    *
    * This function can be used to pass through a successful result while handling an error.
    */
  def mapErr[F](f: E => F): Result[V, F] = this match {
    case Ok(v)  => Ok(v)
    case Err(e) => Err(f(e))
  }

  /**
    * Allows merging of the ok and error values into a type `T`.
    *
    * `T` must be a common super type of both `V` and `E`.
    */
  def merge[T](implicit ev: V <:< T, ev2: E <:< T): T = this match {
    case Ok(v)  => v
    case Err(e) => e
  }

  /**
    * Returns `res` if the result is [[com.lkroll.common.result.Ok Ok]],
    * otherwise returns the [[com.lkroll.common.result.Err Err]] value of `this`.
    */
  def and[T, E1 >: E](res: Result[T, E1]): Result[T, E1] = this match {
    case Ok(_)  => res
    case Err(_) => this.asInstanceOf[Result[T, E1]]
  }

  /**
    * Returns `res` if the result is [[com.lkroll.common.result.Err Err]],
    * otherwise returns the [[com.lkroll.common.result.Ok Ok]] value of `this`.
    *
    * Arguments passed to or are eagerly evaluated;
    * if you are passing the result of a function call,
    * it is recommended to use [[com.lkroll.common.result.Result#orElse]],
    * which is lazily evaluated.
    */
  def or[V1 >: V, E1 >: E](res: Result[V1, E1]): Result[V1, E1] = this match {
    case Ok(_)  => this.asInstanceOf[Result[V1, E1]]
    case Err(_) => res
  }

  /**
    * Calls `op` if the result is [[com.lkroll.common.result.Ok Ok]],
    * otherwise returns the [[com.lkroll.common.result.Err Err]] value of `this`.
    *
    * This function can be used for control flow based on `Result` values.
    */
  def flatMap[T, E1 >: E](op: V => Result[T, E1]): Result[T, E1] = this match {
    case Ok(v)  => op(v)
    case Err(_) => this.asInstanceOf[Result[T, E1]]
  }

  /**
    * Calls `op` if the result is [[com.lkroll.common.result.Ok Ok]],
    * otherwise returns the [[com.lkroll.common.result.Err Err]] value of `this`.
    *
    * Alias of [[com.lkroll.common.result.Result#flatMap]].
    */
  def andThen[T, E1 >: E](op: V => Result[T, E1]): Result[T, E1] = flatMap(op);

  /**
    * Calls `op` if the result is [[com.lkroll.common.result.Err Err]],
    * otherwise returns the [[com.lkroll.common.result.Ok Ok]] value of `this`.
    *
    * This function can be used for control flow based on result values.
    */
  def orElse[V1 >: V, F](op: E => Result[V1, F]): Result[V1, F] = this match {
    case Ok(_)  => this.asInstanceOf[Result[V1, F]]
    case Err(e) => op(e)
  }

  /**
    * Unwraps a result, yielding the content of an [[com.lkroll.common.result.Ok Ok]].
    * Else, it returns `default`.
    */
  def getOrElse[V1 >: V](default: => V1): V1 = this match {
    case Ok(v)  => v
    case Err(_) => default
  }

  /**
    * Unwraps a result, yielding the content of an [[com.lkroll.common.result.Ok Ok]].
    *
    * @throws java.util.NoSuchElementException for [[com.lkroll.common.result.Err Err]]
    * using a standard error message plus the content of the `Err`.
    */
  def get[V1 >: V]: V1 = this match {
    case Ok(v)  => v
    case Err(e) => throw new NoSuchElementException(s"Result.get on Err($e)")
  }

  /**
    * Unwraps a result, yielding the content of an [[com.lkroll.common.result.Ok Ok]].
    *
    * @throws java.util.NoSuchElementException for [[com.lkroll.common.result.Err Err]]
    * using the provided error message.
    */
  def expect[V1 >: V](msg: String): V1 = this match {
    case Ok(v)  => v
    case Err(_) => throw new NoSuchElementException(msg)
  }

  /**
    * Unwraps a result, yielding the content of an [[com.lkroll.common.result.Err Err]].
    * Else, it returns `default`.
    */
  def getErrOrElse[E1 >: E](default: => E1): E1 = this match {
    case Ok(_)  => default
    case Err(e) => e
  }

  /**
    * Unwraps a result, yielding the content of an [[com.lkroll.common.result.Err Err]].
    *
    * @throws java.util.NoSuchElementException for [[com.lkroll.common.result.Ok Ok]]
    * using a standard error message plus the content of the `Ok`.
    */
  def getErr[E1 >: E]: E1 = this match {
    case Ok(v)  => throw new NoSuchElementException(s"Result.getErr on Ok($v)")
    case Err(e) => e
  }

  /**
    * Unwraps a result, yielding the content of an [[com.lkroll.common.result.Err Err]].
    *
    * @throws java.util.NoSuchElementException for [[com.lkroll.common.result.Ok Ok]]
    * using the provided error message.
    */
  def expectErr[E1 >: E](msg: String): E1 = this match {
    case Ok(_)  => throw new NoSuchElementException(msg)
    case Err(e) => e
  }
}

object Result {

  /**
    * Attempts to evaluate `f` and wrap the result in a `Result`.
    *
    * If the evaluation is successful with result `v`, returns `Ok(v)`.
    *
    * If an exception `e` is thrown during evaluation, returns `Err(e)`.
    */
  def apply[V](f: => V): Result[V, Throwable] = fromTry(Try(f))

  /**
    * Converts an `Try` to a `Result`.
    *
    * Maps `Success` to `Ok` and `Failure` to `Err`.
    */
  def fromTry[V](t: Try[V]): Result[V, Throwable] = t match {
    case Success(v) => Ok(v)
    case Failure(e) => Err(e)
  };

  /**
    * Converts an `Option` to a `Result`.
    *
    * If `o` is `Some(v)`, returns `Ok(v)`.
    *
    * If `o` is `None`, returns `Err(e)`,
    * where `e` is the same error message `o.get` would have thrown.
    */
  def fromOption[V](o: Option[V]): Result[V, Throwable] = fromTry(Try(o.get));
}

final case class Ok[+V, +E](value: V) extends Result[V, E] {
  override def isOk: Boolean = true;
  override def isErr: Boolean = false;
}

final case class Err[+V, +E](error: E) extends Result[V, E] {
  override def isOk: Boolean = false;
  override def isErr: Boolean = true;
}
