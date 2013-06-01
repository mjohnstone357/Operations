package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

/**
 * Function which ignores input and returns the String "Hello, World".
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:06
 */
public class HelloWorld implements Function{


    @Override
    public String apply() {
        return "Hello, World";
    }

    @Override
    public void setInput(String input) {

    }
}
