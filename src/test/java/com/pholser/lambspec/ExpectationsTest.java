package com.pholser.lambspec;

import java.util.Objects;
import java.util.function.Predicate;

import org.junit.Test;

import static com.pholser.lambspec.Lambspec.*;
import static com.pholser.lambspec.Subject.*;
import static java.util.Arrays.*;
import static java.util.function.Predicate.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ExpectationsTest {
    @Test public void metExpectation() {
        expect("foo").to(s -> s.startsWith("f"));
    }

    @Test public void unmetExpectation() {
        try {
            expect("foo").to(s -> s.startsWith("d"));
        } catch (AssertionError expected) {
            assertThat(expected.getMessage(), startsWith("[foo] did not match [" + getClass().getName()));
        }
    }

    @Test public void unmetExpectationWithPredicateThatOverridesToString() {
        try {
            expect("foo").to(StartsWith.startsWith("d"));
        } catch (AssertionError expected) {
            assertEquals("[foo] did not match [a string that starts with [d]]", expected.getMessage());
        }
    }

    @Test public void metExpectationWithSatisfySugaring() {
        expect("foo").to(satisfy(s -> s.startsWith("f")));
    }

    @Test public void metExpectationConsistingOfDisjunctionOfPredicates() {
        expect("foo").to(satisfyAny(s -> s.endsWith("g"), s -> s.startsWith("f")));
    }

    @Test public void metExpectationConsistingOfConjunctionOfPredicates() {
        expect("foo").to(satisfyAll(s -> s.endsWith("o"), s -> s.startsWith("f")));
    }

    @Test public void metExpectationConsistingOfAlternativeConjunctionOfPredicates() {
        expect("foo").to(s -> s.endsWith("o")).to(s -> s.startsWith("f"));
    }

    @Test public void allItemsInSubjectSatisfyingAllOfASetOfPredicates() {
        expectEvery(asList("foo", "fungo", "faro")).to(s -> s.endsWith("o")).to(s -> s.startsWith("f"));
    }

    @Test public void atLeastOneItemInSubjectSatisfyingAllOfASetOfPredicates() {
        expectAtLeastOneOf(asList("a", "b", "c")).to(isEqual("b"));
    }

    @Test public void instanceOfExpectation() {
        Object o = "asdf";

        expect(o).to(s -> s instanceof String);
    }

    @Test public void howToDoEqualTo() {
        expect(2).to(satisfy(isEqual(Integer.parseInt("2"))));
    }

    @Test public void howToDoAny() {
        expect(2).to(Integer.class::isInstance);
    }

    @Test public void expectationAlwaysMet() {
        expect(new Object()).to(alwaysTrue);
    }

    @Test(expected = AssertionError.class) public void expectationNeverMet() {
        expect(new Object()).to(alwaysFalse);
    }

    @Test public void subjectHasAtLeastSomeSetOfItems() {
        expect(asList("a", "b", "c")).to(have("b")).to(have("c"));
    }

    @Test public void subjectHasAtLeastOneItemSatisfyingAPredicate() {
        expect(asList("a", "bb", "ccc")).to(haveAnItemSatisfying(s -> s.length() == 2));
    }

    @Test public void subjectHasAtLeastOneItemSatisfyingManyPredicates() {
        expect(asList("a", "bb", "ccc"))
                .to(haveAnItemSatisfying(s -> s.length() == 2))
                .to(haveAnItemSatisfying(s -> s.length() == 3));
    }

    @Test public void fluentNot() {
        expect(2).to(Lambspec.not(Long.class::isInstance));
    }

    @Test public void notNull() {
        expect(2).to(Objects::nonNull);
    }

    @Test public void howToDoSameInstance() {
        Object o = new Object();

        expect(o).to(p -> o == p);
    }

    static class StartsWith implements Predicate<String> {
        private final String prefix;

        StartsWith(String prefix) {
            this.prefix = prefix;
        }

        @Override public boolean test(String s) {
            return s.startsWith(prefix);
        }

        @Override public String toString() {
            return String.format("a string that starts with [%s]", prefix);
        }

        static StartsWith startsWith(String prefix) {
            return new StartsWith(prefix);
        }
    }
}
