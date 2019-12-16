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
package com.larskroll.common.conversions

import java.util.concurrent.{Future => JFuture}
import scala.concurrent.{Future => SFuture}
import scala.concurrent.Promise
import scala.util.Try
import com.google.common.util.concurrent._

object FutureConversions {
  implicit def java2scalaFuture[T](jfuture: JFuture[T]): SFuture[T] = {
    val promise = Promise[T]()
    new Thread(new Runnable {
      def run(): Unit = {
        promise.complete(Try { jfuture.get })
      }
    }).start
    return promise.future;
  }
  implicit class RichListenableFuture[T](lf: ListenableFuture[T]) {
    def toPromise: SFuture[T] = {
      val p = Promise[T]()
      Futures.addCallback(
        lf,
        new FutureCallback[T] {
          def onFailure(t: Throwable): Unit = p failure t
          def onSuccess(result: T): Unit = p success result
        },
        java.util.concurrent.Executors.newSingleThreadExecutor()
      )
      p.future
    }
  }
}
