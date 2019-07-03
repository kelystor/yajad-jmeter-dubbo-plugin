package com.yajad.jmeter.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JsonUtils
 */
public class JsonUtils {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

	public static String toJson(Object obj) {
        return gson.toJson(obj);
	}
}
