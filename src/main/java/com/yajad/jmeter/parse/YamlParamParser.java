package com.yajad.jmeter.parse;

import com.yajad.jmeter.dto.DubboParamDto;
import com.yajad.yaml.resolver.YajadResolver;
import com.yajad.yaml.type.YajadType;
import org.apache.commons.lang3.StringUtils;
import org.snakeyaml.engine.v1.api.Load;
import org.snakeyaml.engine.v1.api.LoadSettings;
import org.snakeyaml.engine.v1.api.LoadSettingsBuilder;
import org.snakeyaml.engine.v1.constructor.YajadConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlParamParser {
    @SuppressWarnings("unchecked")
    public static DubboParamDto parseParameter(String yaml) {
        if (StringUtils.isBlank(yaml)) {
            return new DubboParamDto(new ArrayList<>(), new ArrayList<>());
        }

        LoadSettings settings = new LoadSettingsBuilder().setScalarResolver(new YajadResolver()).build();
        Load load = new Load(settings, new YajadConstructor(settings));
        Object params = load.loadFromString(yaml);

        List<String> dubboParamTypes = new ArrayList<>();
        List<Object> dubboParamValues = new ArrayList<>();

        if (params instanceof Map) {
            //        com.xx.param1:
            //            a: 1
            //            b: 2
            //        com.xxx.param2:
            //            c: 1
            //            d: 2
            // 纯Map项
            List<Object> paramList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) params).entrySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put(entry.getKey(), entry.getValue());
                paramList.add(map);
            }
            params = paramList;
        }
        if (params instanceof List) {
            //        - com.xx.param1:
            //            a: 1
            //            b: 2
            //        - com.xxx.param2:
            //            c: 1
            //            d: 2
            //        - 1
            //        - 2
            //        - abc
            // 列表项有Map的
            for (Object object : (List<Object>) params) {
                Object value = object;
                String typeName;
                if (object instanceof Map) {
                    Map.Entry<String, Object> entry = ((Map<String, Object>) object).entrySet().iterator().next();
                    typeName = entry.getKey();
                    value = entry.getValue();
                    if (value instanceof Map) {
                        // 如果值是Map，说明是自定义的Class，尝试对其转换成对应的Bean实例
                        // 之所以这么做，是因为像dubbox这样的版本，仅仅是直接把Map值传递到服务端的话，
                        // 如果对应的Bean私有字段没有setter方法，服务端并不会把值赋给字段（标准版的dubbo是可以的）
                        // 而如果是在传递给服务端之前就把相应的实例生成好，并赋值好，再把实例传递过去，则不会有这样的问题
                        // 但这要求使用时，要导入对应的jar包
                        value = YajadType.parseMapToBean((Map<String, Object>) value, typeName);
                    } else if (YajadType.isArray(typeName)) {
                        value = YajadType.parseArrayObject((List<?>) value, typeName);
                    }
                } else {
                    typeName = value.getClass().getCanonicalName();
                }
                dubboParamTypes.add(YajadType.normalizerClassName(typeName));
                dubboParamValues.add(value);
            }
        }

        return new DubboParamDto(dubboParamTypes, dubboParamValues);
    }
}
