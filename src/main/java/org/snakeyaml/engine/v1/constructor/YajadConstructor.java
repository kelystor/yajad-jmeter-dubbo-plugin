package org.snakeyaml.engine.v1.constructor;

import com.yajad.yaml.node.ConstructDateClass;
import com.yajad.yaml.node.ConstructLocalDateClass;
import com.yajad.yaml.node.ConstructLocalDateTimeClass;
import com.yajad.yaml.node.ConstructLongClass;
import com.yajad.yaml.tag.YajadTag;
import org.snakeyaml.engine.v1.api.ConstructNode;
import org.snakeyaml.engine.v1.api.LoadSettings;
import org.snakeyaml.engine.v1.exceptions.ConstructorException;
import org.snakeyaml.engine.v1.nodes.Node;
import org.snakeyaml.engine.v1.nodes.ScalarNode;
import org.snakeyaml.engine.v1.nodes.Tag;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
        public Object construct(Node node) {
            String nodeValue = ((ScalarNode) node).getValue();
            String tagValue = node.getTag().getValue();

            // yaml.org,2002:java.math.BigDecimal
            String[] tagSection = tagValue.split(":");
            String className = tagSection[tagSection.length - 1];

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException ignore) {
                throw new ConstructorException(null, Optional.empty(), className + " not found", node.getStartMark());
            }
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
    }
}
