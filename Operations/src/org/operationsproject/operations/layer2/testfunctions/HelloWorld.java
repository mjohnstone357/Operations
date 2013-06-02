package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Function which ignores input and returns the String "Hello, World".
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:06
 */
public class HelloWorld implements Function{


    @Override
    public List<String> apply() {
        List<String> outputs = new ArrayList<>();
        outputs.add("Hello, World");
        return outputs;
    }

    @Override
    public void setInputs(List<String> inputs) {

    }

}
