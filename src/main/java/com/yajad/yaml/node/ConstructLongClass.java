package com.yajad.yaml.node;

import org.snakeyaml.engine.v1.api.ConstructNode;
import org.snakeyaml.engine.v1.nodes.Node;
import org.snakeyaml.engine.v1.nodes.ScalarNode;

public class ConstructLongClass implements ConstructNode {
    @Override
    public Object construct(Node node) {
        String longValue = ((ScalarNode)node).getValue();
        if ((longValue.endsWith("l") || longValue.endsWith("L"))) {
            longValue = longValue.substring(0, longValue.length() - 1);
        }
        return Long.parseLong(longValue);
    }
}
