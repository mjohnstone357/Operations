package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.List;

/**
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:27
 */
public class Identity implements Function{

    private List<String> inputs;

    @Override
    public List<String> apply() {
        assert inputs != null;
        return inputs;
    }

    @Override
    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }
}
