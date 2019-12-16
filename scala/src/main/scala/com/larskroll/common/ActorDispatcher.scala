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
package com.larskroll.common

import akka.actor._

class ActorDispatcher(val actor: Actor) extends scala.concurrent.ExecutionContext {
  override def execute(runnable: Runnable): Unit = {
    actor.self ! ActorDispatcher.ExecuteMe(runnable);
  }

  override def reportFailure(cause: Throwable): Unit = {
    actor.self ! ActorDispatcher.ExecuteError(cause);
  }
}

object ActorDispatcher {
  case class ExecuteMe(runnable: Runnable)
  case class ExecuteError(error: Throwable)

  val receive: PartialFunction[Any, Unit] = {
    case ExecuteMe(runnable) => runnable.run();
    case ExecuteError(error) => System.err.println(error);
  }
}
