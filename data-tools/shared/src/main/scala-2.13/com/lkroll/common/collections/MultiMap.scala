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
package com.lkroll.common.collections

/** An alternative <code>MultiMap</code> trait that uses multi-map semantics for the default map operations.
  *
  * For the standard library version, see [[scala.collection.mutable.MultiMap]].
  *
  * @tparam A key type
  * @tparam B value type (that is the values of the inner collection)
  */
trait MultiMap[A, B] extends Iterable[(A, Iterable[B])] {

  type InnerCollection <: Iterable[B];
  type KeySet <: collection.Set[A];

  def put(kv: (A, B)): Unit;
  def +=(kv: (A, B)): MultiMap.this.type = {
    this.put(kv);
    this
  }
  def putAll(kv: (A, IterableOnce[B])): Unit;
  def ++=(kv: (A, IterableOnce[B])): MultiMap.this.type = {
    this.putAll(kv);
    this
  }
  def remove(kv: (A, B)): Boolean;
  def -=(kv: (A, B)): MultiMap.this.type = {
    this.remove(kv);
    this
  }
  def remove(key: A): Option[InnerCollection];
  def -=(key: A): MultiMap.this.type = {
    this.remove(key);
    this
  }
  def get(key: A): Option[InnerCollection];
  def apply(key: A): InnerCollection = get(key).get;
  override def iterator: Iterator[(A, InnerCollection)];
  def contains(key: A): Boolean;
  def entryExists(key: A, p: B => Boolean): Boolean;
  def keySet: KeySet;
}
