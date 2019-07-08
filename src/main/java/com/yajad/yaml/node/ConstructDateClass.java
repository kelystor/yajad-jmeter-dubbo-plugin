package com.yajad.yaml.node;

import org.snakeyaml.engine.v1.api.ConstructNode;
import org.snakeyaml.engine.v1.nodes.Node;
import org.snakeyaml.engine.v1.nodes.ScalarNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ConstructDateClass implements ConstructNode {
    @Override
    public Object construct(Node node) {
        String dateValue = ((ScalarNode)node).getValue();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(dateValue);
        } catch (ParseException e) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return formatter.parse(dateValue);
            } catch (ParseException e1) {
                return null;
            }
        }
    }
}
