package com.easychat.sse.utils;

import com.easychat.sse.exception.CustomRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new CustomRuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T readValue(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            throw new CustomRuntimeException(e.getMessage(), e);
        }
    }
}
