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
 * Sugar methods/fields for creating expectations in the form of {@link Predicate}s.
 */
public class Lambspec {
    private Lambspec() {
        throw new UnsupportedOperationException();
    }

    /** Predicate that answers {@code true} no matter its argument. */
    public static final Predicate<Object> alwaysTrue = o -> true;

    /** Predicate that answers {@code false} no matter its argument. */
    public static final Predicate<Object> alwaysFalse = alwaysTrue.negate();

    /**
     * <p>When the argument to {@link Subject#must(Predicate)} is a lambda expression or named in a way that doesn't read
     * fluently, this method can help.</p>
     *
     * <p>For example:</p>
     *
     * <pre>subject("foo").must(<strong>satisfy(s -&gt; s.length() == 3)</strong>);</pre>
     *
     * @param p a predicate
     * @param <S> the type of the argument to the predicate
     * @return p
     */
    public static <S> Predicate<S> satisfy(Predicate<S> p) {
        return p;
    }

    /**
     * <p>Creates a disjunction predicate from many other predicates.</p>
     *
     * <p>This method is sugar for chained calls to {@link Predicate#or(Predicate)}.</p>
     *
     * <p>For example:</p>
     *
     * <pre>subject("foo").must(<strong>satisfyAny(s -&gt; s.length() == 3, s -&gt; s.startsWith("x")</strong>);</pre>
     *
     * @param first a predicate
     * @param rest zero or more predicates
     * @param <S> a constraint on the type of the arguments to the predicates
     * @return a predicate that is the disjunction of the given predicates
     */
    @SafeVarargs public static <S> Predicate<S> satisfyAny(Predicate<S> first, Predicate<? super S>... rest) {
        Predicate<S> disjunction = first;
        for (Predicate<? super S> each : rest)
            disjunction = disjunction.or(each);
        return disjunction;
    }

    /**
     * <p>Creates a conjunction predicate from many other predicates.</p>
     *
     * <p>This method is sugar for chained calls to {@link Predicate#and(Predicate)}.</p>
     *
     * <p>For example:</p>
     *
     * <pre>subject("foo").must(<strong>satisfyAll(s -&gt; s.length() == 3, s -&gt; s.startsWith("f")</strong>);</pre>
     *
     * @param first a predicate
     * @param rest zero or more predicates
     * @param <S> a constraint on the type of the arguments to the predicates
     * @return a predicate that is the conjunction of the given predicates
     */
    @SafeVarargs public static <S> Predicate<S> satisfyAll(Predicate<S> first, Predicate<? super S>... rest) {
        Predicate<S> conjunction = first;
        for (Predicate<? super S> each : rest)
            conjunction = conjunction.and(each);
        return conjunction;
    }

    /**
     * Creates a predicate that decides whether an element of its {@link Iterable} argument satisfies the
     * given predicate.
     *
     * @param p the predicate to apply to elements of a sequence
     * @param <S> a constraint on the type of the elements of the sequence
     * @return a predicate that tests elements of the sequence against the given predicate
     */
    public static <S> Predicate<Iterable<S>> haveAnItemSatisfying(Predicate<? super S> p) {
        return items -> {
            for (S each : items) {
                if (p.test(each))
                    return true;
            }
            return false;
        };
    }

    /**
     * Creates a predicate that decides whether a given item is in the given sequence.
     *
     * @param item the item to look for in the sequence
     * @param <S> the type of the item
     * @return a predicate that tests whether the given item is in a sequence
     */
    public static <S> Predicate<Iterable<S>> have(S item) {
        return haveAnItemSatisfying(be(item));
    }

    /**
     * <p>Creates a predicate that decides whether a given item is {@linkplain Object#equals(Object) equal to}
     * another.</p>
     *
     * <p>This is sugar for {@link Predicate#isEqual(Object)}, and reads better than it as an argument to
     * {@link Subject#must(Predicate)}.</p>
     *
     * @param other an item to compare to another
     * @param <S> the type of the item
     * @return a predicate that tests whether the given item equals another
     */
    public static <S> Predicate<S> be(S other) {
        return Predicate.isEqual(other);
    }

    /**
     * <p>Creates a predicate that is the negation of the given predicate.</p>
     *
     * <p>This method is sugar for {@link Predicate#negate()}.</p>
     *
     * @param p a predicate
     * @param <S> the type of the argument to the predicate
     * @return the negation of the given predicate
     */
    public static <S> Predicate<S> not(Predicate<S> p) {
        return p.negate();
    }
}
