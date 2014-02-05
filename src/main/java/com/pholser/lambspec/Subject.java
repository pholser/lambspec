package com.pholser.lambspec;

import java.util.function.Predicate;

public abstract class Subject<S> {
    protected Subject() {
    }

    public static <T> Subject<T> expect(T subject) {
        return new Subject<T>() {
            @Override protected void test(Predicate<? super T> p) {
                if (!p.test(subject))
                    fail("[%s] did not satisfy [%s]", subject, p);
            }
        };
    }

    public static <T> Subject<T> expectEvery(Iterable<T> subject) {
        return new Subject<T>() {
            @Override protected void test(Predicate<? super T> p) {
                for (T each : subject) {
                    if (!p.test(each))
                        fail("[%s] from sequence [%s] did not satisfy [%s]", each, subject, p);
                }
            }
        };
    }

    public static <T> Subject<T> expectAtLeastOneOf(Iterable<T> subject) {
        return new Subject<T>() {
            @Override protected void test(Predicate<? super T> p) {
                for (T each : subject) {
                    if (p.test(each))
                        return;
                }
                fail("No item from sequence [%s] satisfied [%s]", subject, p);
            }
        };
    }

    public final Subject<S> to(Predicate<? super S> p) {
        test(p);
        return this;
    }

    protected abstract void test(Predicate<? super S> p);

    protected void fail(String messageTemplate, Object... args) {
        throw new AssertionError(String.format(messageTemplate, args));
    }
}
