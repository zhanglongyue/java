package priv.yue.common.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;


public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonMapper jsonMapper = new JsonMapper(new JsonFactoryBuilder().quoteChar('\'').build());

    static {
        // mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.disable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public JsonUtils() {
    }

    public static ObjectMapper getNewMapper() {
        return new ObjectMapper();
    }

    public static ObjectMapper getNewDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.disable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return mapper;
    }

    /**
     * 使用单引号替代双引号，ES不允许文本带双引号（需要转义）
     * 日志不需要做读取转换，所以使用单引号没有影响，但是注意这是非标准的json格式。
     * https://github.com/FasterXML/jackson-core/issues/617#issuecomment-713236769
     */
    public static <T> String toJsonWithSingleQuotes(T t) {
        if (Objects.isNull(t))
            return null;
        try {
            return jsonMapper == null ? jsonMapper.writeValueAsString(t) : jsonMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String toJson(T t) {
        return toJson(t, null);
    }

    public static <T> T jsonTo(String json, TypeReference<T> typeReference) {
        return jsonTo(json, typeReference, null);
    }

    public static <T> T jsonToBean(String json, Class<T> clazz) {
        return jsonToBean(json, clazz, null);
    }

    public static <T> T fileToBean(File file, Class<T> clazz) {
        return fileToBean(file, clazz, null);
    }

    public static <T> String toJson(T t, ObjectMapper objectMapper) {
        if (Objects.isNull(t))
            return null;
        try {
            return objectMapper == null ? mapper.writeValueAsString(t) : objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonToBean(String json, Class<T> clazz, ObjectMapper objectMapper) {
        if (StrUtil.isBlank(json))
            return null;
        try {
            return objectMapper == null ? mapper.readValue(json, clazz) : objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fileToBean(File file, Class<T> clazz, ObjectMapper objectMapper) {
        if (!file.exists())
            return null;
        try {
            return objectMapper == null ? mapper.readValue(file, clazz) : objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonTo(String json, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        if (StrUtil.isBlank(json))
            return null;
        try {
            return objectMapper == null ? mapper.readValue(json, typeReference) : objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonTo(String json, Class<T> clazz, ObjectMapper objectMapper) {
        if (StrUtil.isBlank(json))
            return null;
        try {
            return mapper.readValue(json, new TypeReference<T>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
