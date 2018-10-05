/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Lars Kroll <bathtor@googlemail.com>
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
package com.lkroll.common.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context;

object Macros {

  class Impl(val c: Context) {
    def memberList[T: c.WeakTypeTag]: c.Tree = {
      import c.universe._

      val outputType = weakTypeTag[T].tpe;

      val owner = c.internal.enclosingOwner;
      //println(s"Owner ${owner.fullName}");
      val enclosingClass = if (owner.isClass) {
        //println(s"Owner is class ${owner.asClass}");
        owner.asClass
      } else if (owner.owner.isClass) {
        //println(s"Owner's owner is class ${owner.owner.asClass}");
        owner.owner.asClass
      } else {
        println(s"Oh noes...the owner is weird: ${owner.owner.fullName}");
        return q"???"
      };
      val enclosingType: Type = enclosingClass.asType.toType;

      //      val entries = ..collect {
      //        case m => println(s"found a ${c.internal.enclosingOwner.}"); q"null"
      //      }.toList;
      val entries: List[Tree] = enclosingType.members.flatMap {
        case m if ((m != owner) && (!m.isType)) => {
          val t = m.asTerm;
          val termInfo = t.info;
          //println(s"Member: ${t.fullName} -> $termInfo");
          if (termInfo <:< outputType) {
            //println(s"Weapon Member: ${t}");
            val name = t.name;
            Some(q"$name")
          } else {
            None
          }
        }
        case _ => None
      }.toList;

      q"List(..$entries)"
    }
  }

  def memberList[T]: List[T] = macro Impl.memberList[T];

}
