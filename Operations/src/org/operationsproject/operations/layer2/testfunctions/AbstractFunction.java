package org.operationsproject.operations.layer2.testfunctions;

import java.util.List;

/**
 * @author Matthew Johnstone
 *         Date: 02/06/13
 *         Time: 15:12
 */
public class AbstractFunction {

    protected List<String> results;
    protected List<String> inputs;

    public List<String> getResults() {
        assert results != null;
        return results;
    }

    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }

}
