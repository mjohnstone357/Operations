package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.ArrayList;

/**
 * Function which ignores input and returns the String "Hello, World".
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:06
 */
public class HelloWorld extends AbstractFunction implements Function {

    @Override
    public void compute() {
        results = new ArrayList<>();
        results.add("Hello, World");
    }

}
