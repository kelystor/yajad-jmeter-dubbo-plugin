package com.yajad.jmeter.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.Map;

public class JsonUtils {
	private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setPrettyPrinting().disableHtmlEscaping().serializeNulls()
            .create();

	public static String toJson(Object obj) {
        return gson.toJson(obj);
	}

    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        try {
            return gson.fromJson(toJson(map), clazz);
        } catch (JsonSyntaxException ignore) {
        }
        return null;
    }

	public static boolean validate(String jsonString) {
		try {
			new JsonParser().parse(jsonString);
		} catch (JsonSyntaxException ignore) {
			return false;
		}
		return true;
	}
}
