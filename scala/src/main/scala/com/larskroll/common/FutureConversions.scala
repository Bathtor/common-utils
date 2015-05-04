package com.larskroll.common

import java.util.concurrent.{ Future => JFuture }
import scala.concurrent.{ Future => SFuture }
import scala.concurrent.Promise
import scala.util.Try
import com.google.common.util.concurrent._

object FutureConversions {
    implicit def java2scalaFuture[T](jfuture: JFuture[T]): SFuture[T] = {
        val promise = Promise[T]()
        new Thread(new Runnable {
            def run() {
                promise.complete(Try { jfuture.get })
            }
        }).start
        return promise.future;
    }
    implicit class RichListenableFuture[T](lf: ListenableFuture[T]) {
        def toPromise: SFuture[T] = {
            val p = Promise[T]()
            Futures.addCallback(lf, new FutureCallback[T] {
                def onFailure(t: Throwable): Unit = p failure t
                def onSuccess(result: T): Unit = p success result
            })
            p.future
        }
    }
}