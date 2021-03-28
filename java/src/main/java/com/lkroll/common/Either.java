/* 
* Copyright 2019 Lars Kroll
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
* associated documentation files (the "Software"), to deal in the Software without restriction, 
* including without limitation the rights to use, copy, modify, merge, publish, distribute, 
* sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.lkroll.common;

import com.google.common.base.Optional;
import java.util.NoSuchElementException;

public abstract class Either<L, R> {

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract R getRight();

    public abstract L getLeft();

    public Optional<R> getOptionalRight() {
        if (this.isRight()) {
            return Optional.fromNullable(this.getRight());
        } else {
            return Optional.absent();
        }
    }

    public Optional<L> getOptionalLeft() {
        if (this.isRight()) {
            return Optional.fromNullable(this.getLeft());
        } else {
            return Optional.absent();
        }
    }

    public static <L, R> Left<L, R> left(L left) {
        return new Left<L, R>(left);
    }

    public static <L, R> Right<L, R> right(R right) {
        return new Right<L, R>(right);
    }

    public static class Left<L, R> extends Either<L, R> {

        private final L left;

        Left(L left) {
            this.left = left;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("Right can't be accessed");
        }

        @Override
        public L getLeft() {
            return this.left;
        }

    }

    public static class Right<L, R> extends Either<L, R> {

        private final R right;

        Right(R right) {
            this.right = right;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public R getRight() {
            return this.right;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("Left can't be accessed");
        }

    }
}
