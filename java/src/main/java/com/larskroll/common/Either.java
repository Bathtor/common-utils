/*
 * This file is part of the Kompics component model runtime.
 *
 * Copyright (C) 2009 Swedish Institute of Computer Science (SICS) 
 * Copyright (C) 2009 Royal Institute of Technology (KTH)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.larskroll.common;

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
