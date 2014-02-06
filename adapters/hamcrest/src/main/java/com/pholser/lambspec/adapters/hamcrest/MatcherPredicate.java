package com.pholser.lambspec.adapters.hamcrest;

import java.util.function.Predicate;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class MatcherPredicate<T> implements Predicate<T> {
    private final Matcher<? super T> matcher;

    private MatcherPredicate(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    public static <U> MatcherPredicate<U> match(Matcher<? super U> matcher) {
        return new MatcherPredicate<>(matcher);
    }

    @Override public boolean test(T subject) {
        return matcher.matches(subject);
    }

    @Override
    public String toString() {
        return StringDescription.toString(matcher);
    }
}
