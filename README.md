# lambspec

Assertion library for Java >= 8


## Downloading

Accessible from Maven central. Coordinates: `com.pholser:lambspec-{module}:{version}`.
You will need at minimum `lambspec-core`.


## Examples

The best place to begin exploring is in test class `ExpectationsTest`.


## Features

* [RSpec](http://rspec.info/)-like syntax for nominating test subjects
and specifying expectations on them:

    `expect("foo").to(s -> s.startsWith("f"));`

* Uses [predicates](http://download.java.net/jdk8/docs/api/java/util/function/Predicate.html)
as the means for specifying expectations on test subjects. `Predicate` is a
[functional interface](http://download.java.net/jdk8/docs/api/java/lang/FunctionalInterface.html),
so you can use lambda expressions and method references to specify the
expectations more succinctly and sweetly.

* Optional: Adapters for [Hamcrest](http://hamcrest.org/JavaHamcrest/)
matchers and [Guava](https://code.google.com/p/guava-libraries/) predicates

* Optional: Similar syntax for establishing
[assumptions](https://github.com/junit-team/junit/wiki/Assumptions-with-assume)
for [JUnit theories](http://junit.org).


## Caveats

When an expectation or assumption is not met, the raised exception's message will
contain the `toString()` of the expectation. A lambda expression's `toString()`
is not terribly pretty or helpful. If evocative assertion error messages are
important to you, you may want to create implementations of `Predicate`,
anonymous or otherwise, and override `toString()` to your liking.

## About

lambspec was written by Paul Holser, and is distributed under the MIT License.

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
