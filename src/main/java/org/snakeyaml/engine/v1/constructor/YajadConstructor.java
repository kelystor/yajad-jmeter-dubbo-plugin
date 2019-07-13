package org.snakeyaml.engine.v1.constructor;

import com.yajad.yaml.node.ConstructDateClass;
import com.yajad.yaml.node.ConstructLocalDateClass;
import com.yajad.yaml.node.ConstructLocalDateTimeClass;
import com.yajad.yaml.node.ConstructLongClass;
import com.yajad.yaml.tag.YajadTag;
import com.yajad.yaml.type.YajadType;
import org.snakeyaml.engine.v1.api.ConstructNode;
import org.snakeyaml.engine.v1.api.LoadSettings;
import org.snakeyaml.engine.v1.exceptions.ConstructorException;
import org.snakeyaml.engine.v1.nodes.Node;
import org.snakeyaml.engine.v1.nodes.ScalarNode;
import org.snakeyaml.engine.v1.nodes.SequenceNode;
import org.snakeyaml.engine.v1.nodes.Tag;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class YajadConstructor extends StandardConstructor {
    public YajadConstructor(LoadSettings settings) {
        super(settings);
        tagConstructors.put(new Tag(LocalDate.class), new ConstructLocalDateClass());
        tagConstructors.put(new Tag(LocalDateTime.class), new ConstructLocalDateTimeClass());
        tagConstructors.put(YajadTag.LONG, new ConstructLongClass());
        tagConstructors.put(YajadTag.DATE, new ConstructDateClass());
    }

    @Override
    ConstructNode getDefaultConstruct(Node node) {
        return new ConstructUndefined();
    }

    public static final class ConstructUndefined implements ConstructNode {
        private String className;

        public Object construct(Node node) {
            className = extractClassName(node);
            if (YajadType.isArray(className)) {
                return parseArrayObject((SequenceNode) node);
            } else {
                return parseScalarObject((ScalarNode) node);
            }
        }

        private String extractClassName(Node node) {
            String tagValue = node.getTag().getValue();
            // yaml.org,2002:java.math.BigDecimal
            String[] tagSection = tagValue.split(":");
            return tagSection[tagSection.length - 1];
        }

        private Object parseScalarObject(ScalarNode node) {
            Class<?> clazz;
            try {
                clazz = Class.forName(YajadType.normalizerBaseClassName(className));
            } catch (ClassNotFoundException ignore) {
                throw new ConstructorException(null, Optional.empty(), className + " not found", node.getStartMark());
            }
            String nodeValue = node.getValue();
            // 检测对应的类是否有valueOf(String)的静态方法，有则调用方法初始化实例
            try {
                Method method = clazz.getMethod("valueOf", String.class);
                return method.invoke(null, nodeValue);
            } catch (Exception ignore) {
            }
            // 检测对应的类是否有String参数的构造方法，有则用该方法初始化实例
            try {
                Constructor<?> constructor = clazz.getConstructor(String.class);
                return constructor.newInstance(nodeValue);
            } catch (Exception ignore) {
            }
            throw new ConstructorException(null, Optional.empty(), "could not determine a constructor for the tag " + node.getTag(), node.getStartMark());
        }

        private Object parseArrayObject(SequenceNode sequenceNode) {
            List<Object> values = sequenceNode.getValue()
                    .stream()
                    .map(node -> new ConstructUndefined().construct(node))
                    .collect(Collectors.toList());
            return YajadType.parseArrayObject(values, className);
        }
    }
}
