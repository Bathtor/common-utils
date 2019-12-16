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

import scala.collection.mutable;

/**
  * An implementation of MultiMap[A,B] with a TreeMap as outer collection and HashSet as inner collection.
  */
@SerialVersionUID(-1017000328094141241L)
class TreeSetMultiMap[A: Ordering, B] extends MultiMap[A, B] with Serializable {

  type InnerCollection = mutable.HashSet[B];
  type KeySet = collection.SortedSet[A];

  private val ord = implicitly[Ordering[A]];
  private val inner: mutable.TreeMap[A, mutable.HashSet[B]] = new mutable.TreeMap;

  override def put(kv: (A, B)): Unit = {
    val set = inner.get(kv._1) match {
      case Some(set) => set
      case None => {
        val set = new mutable.HashSet[B];
        inner += (kv._1 -> set);
        set
      }
    };
    set += kv._2;
  }
  def putAll(kv: (A, IterableOnce[B])): Unit = {
    val set = inner.get(kv._1) match {
      case Some(set) => set
      case None => {
        val set = new mutable.HashSet[B];
        inner += (kv._1 -> set);
        set
      }
    };
    set ++= kv._2;
  }
  override def remove(kv: (A, B)): Boolean = {
    inner.get(kv._1) match {
      case Some(set) => set.remove(kv._2)
      case None      => false
    }
  }
  override def remove(key: A): Option[InnerCollection] = inner.remove(key);
  def get(key: A): Option[InnerCollection] = {
    inner.get(key)
  }
  override def iterator: Iterator[(A, InnerCollection)] = inner.iterator;
  override def contains(key: A): Boolean = inner.contains(key);
  override def entryExists(key: A, p: B => Boolean): Boolean = inner.get(key) match {
    case Some(set) => set.exists(p)
    case None      => false
  }
  override def keySet: KeySet = inner.keySet;

  def firstKey: A = inner.firstKey;

  def lastKey: A = inner.lastKey;

  def floor(needle: A): Option[A] = {
    val it = inner.keysIterator;
    var last: Option[A] = None;
    while (it.hasNext) {
      val k = it.next();
      if (ord.gt(k, needle)) {
        return last;
      } else {
        last = Some(k);
      }
    }
    None
  }

  def ceil(needle: A): Option[A] = {
    val it = inner.keysIteratorFrom(needle);
    if (it.hasNext) {
      Some(it.next())
    } else {
      None
    }
  }

  // override def mkString(start: String, sep: String, end: String): String = inner.mkString(start, sep, end);
  // override def mkString(sep: String): String = inner.mkString(sep);
  // override def mkString: String = inner.mkString;
}

object TreeSetMultiMap {
  def empty[A: Ordering, B]: TreeSetMultiMap[A, B] = new TreeSetMultiMap;
}
