package org.operationsproject.operations.layer2;

import org.junit.Before;
import org.junit.Test;
import org.operationsproject.operations.layer2.testfunctions.HelloWorld;
import org.operationsproject.operations.layer2.testfunctions.Identity;

import static org.junit.Assert.assertEquals;

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
    public void should_evaluate_hello_world_correctly() {

        Function helloWorld = new HelloWorld();
        application.addFunction(helloWorld);

        String result = application.evaluate(helloWorld);
        assertEquals("Hello, World", result);
    }

    @Test
    public void should_evaluate_hello_world_correctly_via_identity_function() {

        Function helloWorld = new HelloWorld();
        Function identity = new Identity();

        application.addFunction(helloWorld);
        application.addFunction(identity);

        application.addDataDependency(helloWorld, identity);

        String result = application.evaluate(identity);
        assertEquals("Hello, World", result);
    }

    @Test
    public void should_evaluate_hello_world_correctly_via_identity_function_three_times() {

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

        String result = application.evaluate(identity3);
        assertEquals("Hello, World", result);
    }

}
