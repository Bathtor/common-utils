package com.lkroll.common.collections

import org.scalatest._
import org.scalatest.flatspec._
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
    val s = map.iterator.map(t => s"${t._1} -> Set${t._2.mkString("(", ", ", ")")}").mkString(",");
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
    val s = map.iterator.map(t => s"${t._1} -> Set${t._2.mkString("(", ", ", ")")}").mkString(",");
    s should (be("1 -> Set(c),0 -> Set(a, b)") or be("0 -> Set(a, b),1 -> Set(c)"));
  }

}
