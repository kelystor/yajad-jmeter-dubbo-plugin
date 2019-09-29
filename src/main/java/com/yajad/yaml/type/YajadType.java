package com.yajad.yaml.type;

import com.yajad.jmeter.util.JsonUtils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YajadType {
    private static final Map<String, String> TYPE_ALIAS_MAP = new HashMap<>();

    static {
        TYPE_ALIAS_MAP.put("int", "int");
        TYPE_ALIAS_MAP.put("double", "double");
        TYPE_ALIAS_MAP.put("short", "short");
        TYPE_ALIAS_MAP.put("float", "float");
        TYPE_ALIAS_MAP.put("long", "long");
        TYPE_ALIAS_MAP.put("byte", "byte");
        TYPE_ALIAS_MAP.put("bool", "boolean");
        TYPE_ALIAS_MAP.put("boolean", "boolean");
        TYPE_ALIAS_MAP.put("char", "char");
        TYPE_ALIAS_MAP.put("string", String.class.getName());
        TYPE_ALIAS_MAP.put("str", String.class.getName());
        TYPE_ALIAS_MAP.put("List", List.class.getName());
        TYPE_ALIAS_MAP.put("list", List.class.getName());
        TYPE_ALIAS_MAP.put("Set", Set.class.getName());
        TYPE_ALIAS_MAP.put("set", Set.class.getName());
        TYPE_ALIAS_MAP.put("Map", Map.class.getName());
        TYPE_ALIAS_MAP.put("map", Map.class.getName());
    }

    /**
     * 按别名方式写的，返回对应的基本类型（会保留数组类型，原始类型原样返回）
     * float -> float
     * Float -> java.lang.Float
     * list -> java.util.List
     * int[] -> int[]
     * Integer[] -> java.lang.Integer[]
     */
    public static String normalizerClassName(String typeName) {
        int index = typeName.indexOf("[]");
        if (index == -1) {
            return normalizerBaseClassName(typeName);
        } else {
            return normalizerBaseClassName(typeName) + typeName.substring(index);
        }
    }

    /**
     * 按别名方式写的，返回对应的基本类型（数组类型只会返回对应的基本类型）
     * float -> java.lang.Float
     * list -> java.util.List
     * int[] -> java.lang.Integer
     */
    public static String normalizerBaseClassName(String typeName) {
        typeName = typeName.replace("[]", "");

        if (TYPE_ALIAS_MAP.get(typeName) != null) {
            return TYPE_ALIAS_MAP.get(typeName);
        }

        // 不写包名的，默认为java.lang包下
        if (!typeName.contains(".")) {
            return "java.lang." + typeName;
        }

        return typeName;
    }

    public static boolean isArray(String typeName) {
        return typeName.endsWith("[]");
    }

    public static Object parseArrayObject(List<?> values, String typeName) {
        String arrayBaseClassName = YajadType.normalizerBaseClassName(typeName);

        Class<?> clazz = Object.class;
        try {
            clazz = Class.forName(arrayBaseClassName);
        } catch (ClassNotFoundException ignore) {
        }

        // 动态生成数组
        int length = values.size();
        Object array = Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, values.get(i));
        }

        // 转化成对应类型的数组
        try {
            Class<?> arrayClazz = Class.forName("[L" + arrayBaseClassName + ";");
            return arrayClazz.cast(array);
        } catch (ClassNotFoundException | ClassCastException ignore) {
        }

        return array;
    }

    public static Object parseMapToBean(Map<String, Object> map, String typeName) {
        String className = normalizerBaseClassName(typeName);
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ignore) {
            return map;
        }

        return JsonUtils.fromMap(map, clazz);
    }
}
