/*
 The MIT License

 Copyright (c) 2014 Paul R. Holser, Jr.

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
 * <p>Provides a fluent interface for describing and verifying expectations on a particular test subject.</p>
 *
 * <p>Initiate expectations on a test subject using {@link #expect(Object)}, then chain as many expectations
 * in the form of {@linkplain Predicate predicates} as you like with {@link #to(Predicate)}.</p>
 *
 * @param <S> the type of the test subject
 */
public abstract class Subject<S> {
    protected Subject() {
    }

    /**
     * Establishes the given object as a test subject, against which expectations can be set.
     *
     * @param target an object to test
     * @param <T> the type of the test subject
     * @return a test subject in the fluent interface
     */
    public static <T> Subject<T> expect(T target) {
        return new Subject<T>() {
            @Override protected void test(Predicate<? super T> p) {
                if (!p.test(target))
                    fail("[%s] did not satisfy [%s]", target, p);
            }
        };
    }

    /**
     * Establishes elements of the given sequence as test subjects, all of which must meet subsequent expectations.
     *
     * @param sequence a sequence to test
     * @param <T> the type of the elements of the sequence
     * @return a test subject in the fluent interface
     */
    public static <T> Subject<T> expectEachOf(Iterable<T> sequence) {
        return new Subject<T>() {
            @Override protected void test(Predicate<? super T> p) {
                for (T each : sequence) {
                    if (!p.test(each))
                        fail("[%s] from sequence [%s] did not satisfy [%s]", each, sequence, p);
                }
            }
        };
    }

    /**
     * Establishes elements of the given sequence as test subjects, at least one of which must meet subsequent
     * expectations.
     *
     * @param sequence a sequence to test
     * @param <T> the type of the elements of the sequence
     * @return a test subject in the fluent interface
     */
    public static <T> Subject<T> expectAtLeastOneOf(Iterable<T> sequence) {
        return new Subject<T>() {
            @Override protected void test(Predicate<? super T> p) {
                for (T each : sequence) {
                    if (p.test(each))
                        return;
                }
                fail("No item from sequence [%s] satisfied [%s]", sequence, p);
            }
        };
    }

    /**
     * Establishes an expectation on the condition of the test subject.
     *
     * @param p a predicate that represents the expectation
     * @return self, so that expectations can be chained
     * @throws AssertionError if the expectation is not met
     */
    public final Subject<S> to(Predicate<? super S> p) {
        test(p);
        return this;
    }

    /**
     * Tests the expectation represented by the given predicate.
     *
     * @param p a predicate that represents the expectation
     * @throws AssertionError if the expectation is not met
     */
    protected abstract void test(Predicate<? super S> p);

    /**
     * Helper method for implementers of {@link #test(Predicate)} to create an expectation failure with a particular
     * message.
     *
     * @param messageTemplate a {@linkplain String#format(String, Object...) message format pattern}
     * @param args arguments to the message template
     * @throws AssertionError always
     */
    protected void fail(String messageTemplate, Object... args) {
        throw new AssertionError(String.format(messageTemplate, args));
    }
}
