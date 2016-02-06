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

package com.pholser.lambspec.junit;

import java.util.function.Predicate;

import org.junit.AssumptionViolatedException;

/**
 * <p>Provides a fluent interface for describing and verifying
 * <a href="http://junit.org/">JUnit</a>
 * <a href="https://github.com/junit-team/junit/wiki/Assumptions-with-assume">assumptions</a>.</p>
 *
 * <p>Initiate assumptions on a given object using {@link #assume(Object)},
 * then chain as many conditions that the object is assumed to meet in the
 * form of {@linkplain Predicate predicates} {@link #to(Predicate)}.</p>
 *
 * @param <S> the type of object assumptions are made on
 */
public abstract class Assumption<S> {
    protected Assumption() {
    }

    /**
     * Establishes the given object as a target for assumptions.
     *
     * @param target an object to claim assumptions against
     * @param <T> the type of the assumption target
     * @return an assumption target in the fluent interface
     */
    public static <T> Assumption<T> assume(T target) {
        return new Assumption<T>() {
            @Override protected void test(Predicate<? super T> p) {
                if (!p.test(target))
                    fail("[%s] did not satisfy [%s]", target, p);
            }
        };
    }

    /**
     * Establishes an assumption on the condition of the target object.
     *
     * @param p a predicate that represents the assumption
     * @return self, so that assumptions can be chained
     * @throws AssumptionViolatedException if the assumption does not hold
     */
    public final Assumption<S> to(Predicate<? super S> p) {
        test(p);
        return this;
    }

    /**
     * Tests the assumption represented by the given predicate.
     *
     * @param p a predicate that represents the assumption
     * @throws AssumptionViolatedException if the assumption does not hold
     */
    protected abstract void test(Predicate<? super S> p);

    /**
     * Helper method for implementers of {@link #test(Predicate)} to create
     * an assumption failure with a particular message.
     *
     * @param messageTemplate a {@linkplain String#format(String, Object...)
     * message format pattern}
     * @param args arguments to the message template
     * @throws AssumptionViolatedException always
     */
    protected void fail(String messageTemplate, Object... args) {
        throw new AssumptionViolatedException(
            String.format(messageTemplate, args));
    }
}
