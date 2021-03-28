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

import fastparse._, NoWhitespace._
import util.{Failure, Success, Try}

case class ParseError(f: Parsed.Failure) extends Exception(f.label);

case class ParsedCommand[A](parser: ParsingObject[A],
                            interpreter: A => Unit,
                            usage: String = "",
                            description: String = ""
) extends Command {
  override type Options = A;

  override def fit(s: String): Try[Options] = {
    parse(s, parser.parseOperation(_)) match {
      case Parsed.Success(o, _) => Success(o)
      case f: Parsed.Failure    => Failure(ParseError(f))
    }
  }
  override def execute(o: Options): Unit = interpreter(o);
}

trait ParsedCommands { self: CommandConsole =>
  def parsed[A](parser: ParsingObject[A], usage: String = "", descr: String = "")(
      interpreter: A => Unit
  ): ParsedCommand[A] = {
    val cmd = ParsedCommand(parser, interpreter, usage, descr);
    self.commands ::= cmd;
    cmd
  }
}

trait ParsingObject[A] {
  def parseOperation[_: P]: P[A];
}
