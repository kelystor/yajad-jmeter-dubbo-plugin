package com.yajad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileUtils {
    public static String read(String fileName) {
        try {
            return convertInputStreamToString(
                    Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName))
            );
        } catch (IOException ignore) {
            return null;
        }
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());
    }
}
