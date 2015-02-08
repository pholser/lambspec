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

package com.pholser.lambspec.adapters.hamcrest;

import java.util.function.Predicate;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * A predicate that adapts a <a href="http://hamcrest.org/JavaHamcrest/">Hamcrest</a> matcher.
 *
 * @param <T> a constraint on the type of arguments to the predicate
 */
public class MatcherPredicate<T> implements Predicate<T> {
    private final Matcher<? super T> matcher;

    private MatcherPredicate(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    /**
     * Creates a predicate that adapts the given matcher.
     *
     * @param matcher a matcher
     * @param <U> a constraint on the type of arguments to the matcher
     * @return a corresponding predicate
     */
    public static <U> MatcherPredicate<U> match(Matcher<? super U> matcher) {
        return new MatcherPredicate<>(matcher);
    }

    @Override public boolean test(T subject) {
        return matcher.matches(subject);
    }

    @Override public String toString() {
        return StringDescription.toString(matcher);
    }
}
