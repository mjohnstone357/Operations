package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

/**
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:27
 */
public class Identity implements Function{

    private String input;

    @Override
    public String apply() {
        assert input != null;
        return input;
    }

    @Override
    public void setInput(String input) {
        this.input = input;
    }
}
