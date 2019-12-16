/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Lars Kroll <bathtor@googlemail.com>
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
package com.larskroll.common.collections

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MultiMapTest extends AnyFlatSpec with Matchers {

  "A TreeSetMultiMap" should "allow multiple values" in {
    val map = TreeSetMultiMap.empty[Int, String];
    map += (0 -> "a");
    map += (0 -> "b");
    map += (1 -> "c");
    map(0) should contain("a");
    map(0) should contain("b");
    map(0) should not contain ("c");
    map(1) should contain("c");
    map(1) should not contain ("a");
    map(1) should not contain ("b");
  }

  it should "remove keys and values" in {
    val map = TreeSetMultiMap.empty[Int, String];
    map += (0 -> "a");
    map += (0 -> "b");
    map += (1 -> "c");
    map -= (0 -> "a");
    map -= 1;
    map(0) should not contain ("a");
    map(0) should contain("b");
    map(0) should not contain ("c");
    map.get(1) shouldBe empty;
  }

  it should "have ordered key lookups" in {
    val map = TreeSetMultiMap.empty[Int, String];
    map += (0 -> "a");
    map += (1 -> "b");
    map += (3 -> "c");
    map.firstKey should be(0);
    map.lastKey should be(3);
    map.floor(2) should contain(1);
    map.ceil(2) should contain(3);
  }

  it should "print cool stuff" in {
    val map = TreeSetMultiMap.empty[Int, String];
    map += (0 -> "a");
    map += (0 -> "b");
    map += (1 -> "c");
    val s = map.mkString(",");
    s should be("0 -> Set(a, b),1 -> Set(c)");
  }

  "A HashSetMultiMap" should "allow multiple values" in {
    val map = HashSetMultiMap.empty[Int, String];
    map += (0 -> "a");
    map += (0 -> "b");
    map += (1 -> "c");
    map(0) should contain("a");
    map(0) should contain("b");
    map(0) should not contain ("c");
    map(1) should contain("c");
    map(1) should not contain ("a");
    map(1) should not contain ("b");
  }

  it should "remove keys and values" in {
    val map = HashSetMultiMap.empty[Int, String];
    map += (0 -> "a");
    map += (0 -> "b");
    map += (1 -> "c");
    map -= (0 -> "a");
    map -= 1;
    map(0) should not contain ("a");
    map(0) should contain("b");
    map(0) should not contain ("c");
    map.get(1) shouldBe empty;
  }

  it should "print cool stuff" in {
    val map = HashSetMultiMap.empty[Int, String];
    map += (0 -> "a");
    map += (0 -> "b");
    map += (1 -> "c");
    val s = map.mkString(",");
    s should be("1 -> Set(c),0 -> Set(a, b)");
  }

}
