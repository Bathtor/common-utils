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
package com.larskroll.common.repl

import org.jline.reader._;
import org.jline.terminal.TerminalBuilder;
import util.{Failure, Success, Try}
import org.apache.log4j.{Layout, LogManager, PatternLayout, WriterAppender};
import com.typesafe.scalalogging.Logger

trait Command {

  type Options;

  def fit(s: String): Try[Options];
  def execute(o: Options): Unit;
  def usage: String;
  def description: String;
}

class FittingException(message: String) extends Exception(message) {

  def this(message: String, cause: Throwable) = {
    this(message)
    initCause(cause)
  }

  def this(cause: Throwable) = {
    this(Option(cause).map(_.toString).orNull, cause)
  }

  def this() = {
    this(null: String)
  }
}

case class ExactCommand(command: String, interpreter: Unit => Unit, description: String = "") extends Command {
  override type Options = Unit;

  override def fit(s: String): Try[Options] = {
    if (s.equalsIgnoreCase(command)) {
      Success(())
    } else {
      Failure(new FittingException(s"$command != $s (ignoring case)"))
    }
  }

  override def execute(o: Options): Unit = interpreter(o);

  override def usage: String = command;
}

trait CommandConsole extends Runnable {
  val terminal = TerminalBuilder.terminal();
  val reader = LineReaderBuilder
    .builder()
    .terminal(terminal)
    .build();
  val out = terminal.writer();

  private[repl] var commands: List[Command] = Nil;
  protected var prompt: String = ">";
  private var active: Boolean = true;

  protected def exit(): Unit = {
    active = false;
  }

  def layout: Layout = new PatternLayout("%d{[HH:mm:ss,SSS]} %-5p {%c{1}} %m%n");

  protected def logger: Logger;

  def onInterrupt(): Unit = {
    logger.warn("Console was interrupted.");
  }

  def onEOF(): Unit = {
    logger.warn("Read EOF, stopping...");
    exit();
  }

  override def run(): Unit = {
    val rootL = LogManager.getRootLogger();
    rootL.removeAllAppenders();
    rootL.addAppender(new WriterAppender(layout, out));
    while (active) {
      try {
        val line = reader.readLine(prompt);
        val res = matchCommand(line);
        if (!res) {
          logger.error(s"Unknown command \'$line\'.\nType \'help\' to see available commands.");
        }
      } catch {
        case _: UserInterruptException => onInterrupt();
        case _: EndOfFileException     => onEOF();
      }
    }
  }

  private def matchCommand(line: String): Boolean = {
    commands.foreach { c =>
      c.fit(line) match {
        case Success(o) => c.execute(o); return true;
        case Failure(_) => // ignore
      }
    }
    return false;
  }

  def exact(command: String, descr: String = "")(interpreter: => Unit): ExactCommand = {
    val f = (() => interpreter);
    val i: Unit => Unit = Unit => f();
    val cmd = ExactCommand(command, i, descr);
    commands ::= cmd;
    cmd
  }

  val helpCommand = exact("help", "Displays this help.") {
    val sorted = commands.filterNot(c => c.usage.isEmpty()).sortBy(c => c.usage);
    val padTo = sorted.foldLeft(0) {
      case (acc, c) => Math.max(acc, c.usage.length())
    } + 4;
    val sb = new StringBuilder;
    sb ++= "Available Commands:\n\n";
    sorted.foreach { c =>
      sb ++= c.usage;
      for (_ <- 0 to (padTo - c.usage.length())) {
        sb += ' ';
      }
      sb ++= c.description;
      sb += '\n';
    }
    sb += '\n';
    sb ++= s"There are ${commands.size - sorted.size} undocumented commands.";
    val help = sb.result();
    out.println(help);
  };

  val exitCommand = exact("exit", "Ends the program.") {
    logger.info("Shutting down...");
    exit();
    System.exit(0);
  }

}
