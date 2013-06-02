package org.operationsproject.operations.layer2;

import java.util.List;

/**
 * @author Matthew Johnstone
 *         Date: 01/06/13
 *         Time: 22:56
 */
public interface Function {

    // TODO Apply NotNull annotations
    public List<String> apply();

    public void setInputs(List<String> inputs);

}
