package com.pholser.lambspec.adapters.hamcrest;

import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.pholser.lambspec.Subject.*;
import static com.pholser.lambspec.adapters.hamcrest.MatcherPredicate.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MatcherPredicateTest {
    @Test public void metExpectationUsingHamcrestMatcherAsPredicate() {
        expect("foo").to(match(startsWith("f")));
    }

    @Test public void unmetExpectationUsingHamcrestMatcherAsPredicate() {
        try {
            expect("foo").to(match(startsWith("d")));
        } catch (AssertionError expected) {
            assertEquals("[foo] did not satisfy [" + StringDescription.toString(startsWith("d")) + ']',
                    expected.getMessage());
            return;
        }

        fail();
    }
}
