package com.yajad.yaml.node;

import org.snakeyaml.engine.v1.api.ConstructNode;
import org.snakeyaml.engine.v1.nodes.Node;
import org.snakeyaml.engine.v1.nodes.ScalarNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConstructLocalDateTimeClass implements ConstructNode {
    @Override
    public Object construct(Node node) {
        String dateValue = ((ScalarNode)node).getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateValue, formatter);
    }

}
