package com.pholser.lambspec;

import java.util.function.Predicate;

public class Lambspec {
    private Lambspec() {
        throw new UnsupportedOperationException();
    }

    public static final Predicate<Object> alwaysTrue = o -> true;
    public static final Predicate<Object> alwaysFalse = alwaysTrue.negate();

    public static <S> Predicate<S> satisfy(Predicate<S> p) {
        return p;
    }

    @SafeVarargs public static <T> Predicate<T> satisfyAny(Predicate<T> first, Predicate<? super T>... rest) {
        Predicate<T> disjunction = first;
        for (Predicate<? super T> each : rest)
            disjunction = disjunction.or(each);
        return disjunction;
    }

    @SafeVarargs public static <T> Predicate<T> satisfyAll(Predicate<T> first, Predicate<? super T>... rest) {
        Predicate<T> conjunction = first;
        for (Predicate<? super T> each : rest)
            conjunction = conjunction.and(each);
        return conjunction;
    }

    public static <T> Predicate<Iterable<T>> haveAnItemSatisfying(Predicate<? super T> item) {
        return ts -> {
            for (T each : ts) {
                if (item.test(each))
                    return true;
            }
            return false;
        };
    }

    public static <T> Predicate<Iterable<T>> have(T item) {
        return haveAnItemSatisfying(Predicate.isEqual(item));
    }

    public static <T> Predicate<T> not(Predicate<T> other) {
        return other.negate();
    }
}
