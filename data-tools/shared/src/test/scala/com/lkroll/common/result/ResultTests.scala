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

import org.scalatest._
import org.scalatest.matchers.{ BePropertyMatcher, BePropertyMatchResult }

import scala.util.{ Try, Success, Failure };

object CustomMatchers {
  class OkBePropertyMatcher extends BePropertyMatcher[Result[_, _]] {
    def apply(left: Result[_, _]) = BePropertyMatchResult(left.isOk, "ok")
  }
  class ErrBePropertyMatcher extends BePropertyMatcher[Result[_, _]] {
    def apply(left: Result[_, _]) = BePropertyMatchResult(left.isErr, "err")
  }

  val ok = new OkBePropertyMatcher;
  val err = new ErrBePropertyMatcher;
}

class ResultTests extends FlatSpec with Matchers {
  import CustomMatchers._;

  val r1: Result[Int, String] = Ok(-3);
  val r2: Result[Int, String] = Err("Some error message");

  "A Result" should "identify itself" in {
    r1 should be an (ok);
    r1 should not be an (err);

    r2 should not be an (ok);
    r2 should be an (err);
  }

  it should "extract values" in {
    {
      r1.get should be (-3);
      r1.getOrElse(0) should be (-3);
      r1.expect("error!") should be (-3);

      an[NoSuchElementException] should be thrownBy r2.get;
      val thrown = the[NoSuchElementException] thrownBy r2.expect("test");
      thrown.getMessage should be ("test");
      r2.getOrElse(0) should be (0);
    }
    {
      an[NoSuchElementException] should be thrownBy r1.getErr;
      r1.getErrOrElse(0) should be (0);
      val thrown = the[NoSuchElementException] thrownBy r1.expectErr("test");
      thrown.getMessage should be ("test");

      r2.getErr should be ("Some error message");
      r2.getErrOrElse("no error!") should be ("Some error message");
      r2.expectErr("no error!") should be ("Some error message");
    }
  }

  it should "convert to Option" in {
    r1.ok() should be (Some(-3));
    r1.err() should be (None);

    r2.ok() should be (None);
    r2.err() should be (Some("Some error message"));
  }

  it should "convert to Try" in {
    r1.toTry should be (Success(-3));
    r2.toTry shouldBe a[Failure[_]];
  }

  it should "map and mapErr" in {
    r1.map(Math.abs) should be (Ok(3));
    r2.map(Math.abs) should be (r2);

    r1.mapErr(_.length()) should be (r1);
    r2.mapErr(_.length()) should be (Err(18));
  }

  it should "chain with flatMap / andThen / orElse" in {
    val f1: Int => Result[String, String] = (i: Int) => Ok(i.toString);
    val f2: Int => Result[String, String] = (_: Int) => Err("Oh no!");

    r1.flatMap(f1) should be (Ok("-3"));
    r2.flatMap(f1) should be (r2);
    r1.flatMap(f2) should be (Err("Oh no!"));
    r2.flatMap(f2) should be (r2);

    r1.andThen(f1) should be (Ok("-3"));
    r2.andThen(f1) should be (r2);
    r1.andThen(f2) should be (Err("Oh no!"));
    r2.andThen(f2) should be (r2);

    val f3: String => Result[String, Int] = (_: String) => Ok("Yay!");
    val f4: String => Result[String, Int] = (s: String) => Err(s.length());

    r1.orElse(f3) should be (r1);
    r2.orElse(f3) should be (Ok("Yay!"));
    r1.orElse(f4) should be (r1);
    r2.orElse(f4) should be (Err(18));
  }

  it should "combine with and / or" in {
    {
      val x: Result[Int, String] = Ok(2);
      val y: Result[String, String] = Err("late error");
      (x and y) should be (y);
      (x or y) should be (x);
    }

    {
      val x: Result[Int, String] = Err("early error");
      val y: Result[String, String] = Ok("foo");
      (x and y) should be (x);
      (x or y) should be (y);
    }
    {
      val x: Result[Int, String] = Err("not a 2");
      val y: Result[String, String] = Err("late error");
      (x and y) should be (x);
      (x or y) should be (y);
    }
    {
      val x: Result[Int, String] = Ok(2);
      val y: Result[String, String] = Ok("different result type");
      (x and y) should be (y);
      (x or y) should be (x);
    }
  }

  it should "instantiate from evaluation" in {
    val r = Result("5".toInt);
    r should be (Ok(5));
    val re = Result("nope".toInt);
    re shouldBe an[Err[_, _]];
  }

  it should "convert from Try" in {
    val t = Try("5".toInt);
    val r = Result.fromTry(t);
    r should be (Ok(5));
    r.toTry should be (t);
    val te = Try("nope".toInt);
    val re = Result.fromTry(te);
    re should be an (err);
    re.toTry should be (te);
  }

  it should "convert from Option" in {
    val o: Option[Int] = Some(5);
    val r = Result.fromOption(o);
    r should be (Ok(5));
    r.ok() should be (o);
    val n: Option[Int] = None;
    val rn = Result.fromOption(n);
    rn should be an (err);
    rn.ok() should be (n);
  }

  it should "merge" in {
    val any1: Any = r1.merge;
    any1 should be (-3);
    val any2 = r2.merge[Any];
    any2 should be ("Some error message");

    val rSame: Result[String, String] = Ok("Test");
    val s = rSame.merge;
    s shouldBe a[String];
    s should be ("Test");
  }
}
