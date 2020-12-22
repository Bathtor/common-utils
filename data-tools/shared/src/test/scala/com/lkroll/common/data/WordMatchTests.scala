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
package com.lkroll.common.data

import org.scalatest._
import funsuite._
import matchers._

class WordMatchTests extends AnyFunSuite with should.Matchers {
  val w1 = "Word";
  val w2 = "World";
  val w3 = "Nothing";
  val w4 = "Something";

  test("Edit Distance should work") {
    import EditDistance.editDist;

    editDist(w1, w1) should be(0);
    editDist(w1, w2) should be(1);
    editDist(w3, w4) should be(3);
  }

  test("WordMatching should work") {
    val wm1 = WordMatch.matchForCase(w1, w1);
    wm1.relativeMatch should be(1.0f);
    wm1.relativeDistance should be(0.0f);
    wm1.isSignificant() should be(true);
    val wm2 = WordMatch.matchForIgnoreCase(w1, w1);
    wm2.relativeMatch should be(1.0f);
    wm2.relativeDistance should be(0.0f);
    wm2.isSignificant() should be(true);

    val wm3 = WordMatch.matchForCase(w1, w2);
    wm3.relativeMatch should be < 1.0f;
    wm3.relativeDistance should be > 0.0f;
    wm3.isSignificant() should be(true);

    val wm4 = WordMatch.matchForCase(w1, w4);
    wm4.relativeMatch should be < 0.5f;
    wm4.relativeDistance should be > 0.5f;
    wm4.isSignificant() should be(false);
  }
}
