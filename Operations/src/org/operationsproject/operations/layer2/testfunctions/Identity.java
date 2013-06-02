package org.operationsproject.operations.layer2.testfunctions;

/**
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 23:27
 */
public class Identity extends AbstractFunction {

    @Override
    public void compute() {
        assert inputs != null;
        results = inputs;
    }
}
