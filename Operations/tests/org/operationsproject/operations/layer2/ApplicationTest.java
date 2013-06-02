/*
 * Copyright (c) 2013, Matthew Johnstone
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 	Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 	Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.operationsproject.operations.layer2;

import org.junit.Before;
import org.junit.Test;
import org.operationsproject.operations.graph.CycleException;
import org.operationsproject.operations.graph.UnknownNodeException;
import org.operationsproject.operations.layer2.testfunctions.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:01
 */
public class ApplicationTest {

    private Application application;

    @Before
    public void setUp() {
        application = new Application();
    }

    @Test
    public void should_evaluate_hello_world_correctly() throws UnknownNodeException {

        Function helloWorld = new HelloWorld();
        application.addFunction(helloWorld);

        List<String> results = application.evaluate(helloWorld);
        assertEquals("Hello, World", results.get(0));
    }

    @Test(expected = UnknownNodeException.class)
    public void should_throw_exception_when_asked_to_evaluate_an_unknown_payload() throws UnknownNodeException {
        Function helloWorld = new HelloWorld();
        application.evaluate(helloWorld);
    }

    @Test
    public void should_evaluate_hello_world_correctly_via_identity_function() throws CycleException, UnknownNodeException {

        Function helloWorld = new HelloWorld();
        Function identity = new Identity();

        application.addFunction(helloWorld);
        application.addFunction(identity);

        application.addDataDependency(helloWorld, identity);

        List<String> results = application.evaluate(identity);
        assertEquals("Hello, World", results.get(0));
    }

    @Test
    public void should_evaluate_foobar_concatenation_example_correctly() throws CycleException, UnknownNodeException {

        Function foo = new Foo();
        Function bar = new Bar();
        Function concat = new Concatenate();

        application.addFunction(foo);
        application.addFunction(bar);
        application.addFunction(concat);

        application.addDataDependency(foo, concat);
        application.addDataDependency(bar, concat);

        List<String> results = application.evaluate(concat);
        String actual = results.get(0);
        assertTrue(actual.equals("FooBar") || actual.equals("BarFoo"));
    }

    @Test
    public void should_evaluate_concatenation_of_multiple_string_literals() throws CycleException, UnknownNodeException {

        Function alpha = new StringLiteral("Alpha");
        Function beta = new StringLiteral("Beta");
        Function gamma = new StringLiteral("Gamma");
        Function delta = new StringLiteral("Delta");
        Function omega = new StringLiteral("Omega");

        Function rootConcat = new Concatenate();
        Function concatLeft = new Concatenate();
        Function concatRight = new Concatenate();

        application.addFunction(alpha);
        application.addFunction(beta);
        application.addFunction(gamma);
        application.addFunction(delta);
        application.addFunction(omega);

        application.addFunction(rootConcat);
        application.addFunction(concatLeft);
        application.addFunction(concatRight);

        application.addDataDependency(concatLeft, rootConcat);
        application.addDataDependency(concatRight, rootConcat);
        application.addDataDependency(omega, rootConcat);

        application.addDataDependency(alpha, concatLeft);
        application.addDataDependency(beta, concatLeft);

        application.addDataDependency(gamma, concatRight);
        application.addDataDependency(delta, concatRight);

        List<String> results = application.evaluate(rootConcat);
        String actual = results.get(0);
        assertEquals(24, actual.length());
        assertTrue(actual.contains("Alpha"));
        assertTrue(actual.contains("Beta"));
        assertTrue(actual.contains("Gamma"));
        assertTrue(actual.contains("Delta"));
        assertTrue(actual.contains("Omega"));
    }

    @Test(expected = CycleException.class)
    public void should_prevent_circular_data_dependency() throws CycleException, UnknownNodeException {

        Function helloWorld = new HelloWorld();
        Function identity = new Identity();

        application.addFunction(helloWorld);
        application.addFunction(identity);

        application.addDataDependency(helloWorld, identity);
        application.addDataDependency(identity, helloWorld);

    }

    @Test(expected = UnknownNodeException.class)
    public void should_prevent_unknown_function_from_being_used_as_a_data_dependency_1() throws CycleException, UnknownNodeException {

        Function helloWorld = new HelloWorld();
        Function identity = new Identity();

        application.addFunction(helloWorld);

        application.addDataDependency(helloWorld, identity);
    }

    @Test(expected = UnknownNodeException.class)
    public void should_prevent_unknown_function_from_being_used_as_a_data_dependency_2() throws CycleException, UnknownNodeException {

        Function helloWorld = new HelloWorld();
        Function identity = new Identity();

        application.addFunction(identity);

        application.addDataDependency(helloWorld, identity);
    }

    @Test(expected = UnknownNodeException.class)
    public void should_prevent_unknown_function_from_being_used_as_a_data_dependency_3() throws CycleException, UnknownNodeException {

        Function helloWorld = new HelloWorld();
        Function identity = new Identity();

        application.addDataDependency(helloWorld, identity);
    }

    @Test
    public void should_evaluate_hello_world_correctly_via_identity_function_three_times() throws CycleException, UnknownNodeException {

        Function helloWorld = new HelloWorld();
        Function identity1 = new Identity();
        Function identity2 = new Identity();
        Function identity3 = new Identity();

        application.addFunction(helloWorld);
        application.addFunction(identity1);
        application.addFunction(identity2);
        application.addFunction(identity3);

        application.addDataDependency(helloWorld, identity1);
        application.addDataDependency(identity1, identity2);
        application.addDataDependency(identity2, identity3);

        List<String> results = application.evaluate(identity3);
        assertEquals("Hello, World", results.get(0));
    }

}
