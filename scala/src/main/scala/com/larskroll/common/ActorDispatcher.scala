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