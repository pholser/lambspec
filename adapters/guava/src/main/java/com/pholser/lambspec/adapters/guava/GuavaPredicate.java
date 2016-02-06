/*
 The MIT License

 Copyright (c) 2014-2016 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.lambspec.adapters.guava;

import java.util.function.Predicate;

/**
 * A predicate that adapts a
 * <a href="https://code.google.com/p/guava-libraries/">Guava</a> predicate.
 *
 * @param <T> a constraint on the type of arguments to the predicate
 */
public class GuavaPredicate<T> implements Predicate<T> {
    private final com.google.common.base.Predicate<? super T> adapted;

    private GuavaPredicate(com.google.common.base.Predicate<? super T> adapted) {
        this.adapted = adapted;
    }

    /**
     * Creates a predicate that adapts the given Guava predicate.
     *
     * @param adapted the Guava predicate to adapt
     * @param <U> a constraint on the type of arguments to the predicate
     * @return a corresponding core Java predicate
     */
    public static <U> GuavaPredicate<U> applyTo(
        com.google.common.base.Predicate<? super U> adapted) {

        return new GuavaPredicate<>(adapted);
    }

    @Override public boolean test(T subject) {
        return adapted.apply(subject);
    }

    @Override public String toString() {
        return adapted.toString();
    }
}
