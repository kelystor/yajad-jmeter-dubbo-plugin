package com.yajad.jmeter.parse;

public class ParamParserException extends RuntimeException {
    ParamParserException(String message) {
        super(message);
    }

    ParamParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
