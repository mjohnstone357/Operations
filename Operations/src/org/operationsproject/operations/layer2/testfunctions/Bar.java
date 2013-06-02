package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.ArrayList;
import java.util.List;

public class Bar implements Function{


    @Override
    public List<String> apply() {
        List<String> outputs = new ArrayList<>();
        outputs.add("Bar");
        return outputs;
    }

    @Override
    public void setInputs(List<String> inputs) {

    }
}
