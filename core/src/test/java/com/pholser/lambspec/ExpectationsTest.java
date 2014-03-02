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

import java.util.Objects;
import java.util.function.Predicate;

import com.google.common.base.Strings;
import org.junit.Test;

import static com.pholser.lambspec.Lambspec.*;
import static com.pholser.lambspec.Subject.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;

public class ExpectationsTest {
    @Test public void metExpectation() {
        expect("foo").to(s -> s.startsWith("f"));
    }

    @Test public void unmetExpectation() {
        try {
            expect("foo").to(s -> s.startsWith("d"));
        } catch (AssertionError expected) {
            assertThat(expected.getMessage(), startsWith("[foo] did not satisfy [" + getClass().getName()));
            return;
        }

        fail();
    }

    @Test public void unmetExpectationWithPredicateThatOverridesToString() {
        try {
            expect("foo").to(StartsWith.startsWith("d"));
        } catch (AssertionError expected) {
            assertEquals("[foo] did not satisfy [a string that starts with [d]]", expected.getMessage());
            return;
        }

        fail();
    }

    @Test public void metGuavaExpectation() {
        expect("").to(satisfy(Strings::isNullOrEmpty));
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
        eachOf(asList("foo", "fungo", "faro")).to(s -> s.endsWith("o")).to(s -> s.startsWith("f"));
    }

    @Test public void notAllItemsInSubjectSatisfyingAllOfASetOfPredicates() {
        try {
            eachOf(asList("foo", "fungo", "fare")).to(s -> s.endsWith("o")).to(s -> s.startsWith("f"));
        } catch (AssertionError expected) {
            assertThat(expected.getMessage(),
                    startsWith("[fare] from sequence [[foo, fungo, fare]] did not satisfy [" + getClass().getName()));
            return;
        }

        fail();
    }

    @Test public void atLeastOneItemInSubjectSatisfyingAllOfASetOfPredicates() {
        atLeastOneOf(asList("a", "b", "c")).to(be("b"));
    }

    @Test public void noItemsInSubjectSatisfyingAllOfASetOfPredicates() {
        try {
            atLeastOneOf(asList("a", "b", "c")).to(be("d"));
        } catch (AssertionError expected) {
            assertThat(expected.getMessage(), startsWith("No item from sequence [[a, b, c]] satisfied ["));
            return;
        }

        fail();
    }

    @Test public void instanceOfExpectation() {
        Object o = "asdf";

        expect(o).to(s -> s instanceof String);
    }

    @Test public void howToDoEqualTo() {
        expect(2).to(be(Integer.parseInt("2")));
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

    @Test public void usingInstanceMethodReferencesAsPredicates() {
        expect("foo").to("football"::startsWith);
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
