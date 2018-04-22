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
package com.lkroll.common.data

case class WordMatch(word: String, relativeMatch: Float, relativeDistance: Float) extends Ordered[WordMatch] {
  override def compare(that: WordMatch): Int = {
    if (this.word == that.word) {
      return 0;
    } else {
      val matchDiff = this.relativeMatch - that.relativeMatch;
      if (Math.abs(matchDiff) < WordMatch.magnitude) {
        val distDiff = that.relativeDistance - this.relativeDistance; // for distance lower is better
        Math.signum(distDiff).toInt
      } else {
        Math.signum(matchDiff).toInt
      }
    }
  }

  def isSignificant(): Boolean = relativeMatch > WordMatch.significance;
}

object WordMatch {

  val significance = 0.5f;
  val magnitude = 0.01f;

  def matchForIgnoreCase(needleCase: String, wordCase: String): WordMatch = {
    val needle = needleCase.toLowerCase();
    val word = wordCase.toLowerCase();
    val flen = word.length().toFloat;
    val nlen = needle.length().toFloat;
    val dist = EditDistance.editDist(needle, word);
    val rDist = (dist.toFloat) / flen;
    val lMatch = longestMatch(needle, word);
    val rMatch = (lMatch.toFloat) / nlen;
    WordMatch(wordCase, rMatch, rDist)
  }

  def matchForCase(needle: String, word: String): WordMatch = {
    val flen = word.length().toFloat;
    val nlen = needle.length().toFloat;
    val dist = EditDistance.editDist(needle, word);
    val rDist = (dist.toFloat) / flen;
    val lMatch = longestMatch(needle, word);
    val rMatch = (lMatch.toFloat) / nlen;
    WordMatch(word, rMatch, rDist)
  }

  def perfect(word: String): WordMatch = {
    WordMatch(word, 1.0f, 0.0f)
  }

  private def longestMatch(needle: String, word: String): Int = {
    //println(s"Comparing $needle with $word");
    val maxPossible = Math.min(needle.length(), word.length());
    //println(s"Longest possible match is $maxPossible");
    for (l <- (maxPossible to 0 by -1)) {
      //println(s"Trying $l");
      val prefix = needle.substring(0, l);
      val suffix = needle.substring(needle.length() - l);
      if (word.contains(prefix) || word.contains(suffix)) {
        //println(s"Found $l with $prefix or $suffix");
        return l;
      }
    }
    return 0;
  }
}
