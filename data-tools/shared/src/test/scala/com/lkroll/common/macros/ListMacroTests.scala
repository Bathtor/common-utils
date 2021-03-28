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
package com.lkroll.common.macros

import org.scalatest._
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class ListMacroTests extends AnyFunSuite with Matchers {
  test("Member listing should work.") {
    TestMe.list should contain(1);
    TestMe.list should contain(2);
    TestMe.list should contain(3);
    TestMe.listO should contain(TestMe.TestO);
  }
}

sealed trait MyTrait;
object TestMe {
  val someI = 1;
  val someO = 2;
  val someThing = 3;

  case object TestO extends MyTrait;

  val list: List[Int] = Macros.memberList[Int];
  val listO: List[MyTrait] = Macros.memberList[MyTrait];
}
