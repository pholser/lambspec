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

package com.pholser.lambspec;

import java.util.function.Predicate;

/**
 * <p>A predicate with a description attached.</p>
 *
 * <p>Use {@link #meet(String, Predicate)} when you want a nicer message if
 * your lambda-based expectation fails.</p>
 *
 * @param <T> a constraint on the type of arguments to the predicate
 */
public class DescriptivePredicate<T> implements Predicate<T> {
    private final String description;
    private final Predicate<T> delegate;

    private DescriptivePredicate(String description, Predicate<T> delegate) {
        this.description = description;
        this.delegate = delegate;
    }

    /**
     * Makes a predicate that delegates to another predicate, and responds to
     * {@link Object#toString()} with a given string.
     *
     * @param <U> a constraint on the type of arguments to the predicate
     * @param description how to respond to {@link Object#toString()}
     * @param delegate what predicate to delegate to
     * @return the new descriptive predicate
     */
    public static <U> DescriptivePredicate<U> meet(
        String description,
        Predicate<U> delegate) {

        return new DescriptivePredicate<>(description, delegate);
    }

    @Override public boolean test(T target) {
        return delegate.test(target);
    }

    @Override public String toString() {
        return description;
    }
}
