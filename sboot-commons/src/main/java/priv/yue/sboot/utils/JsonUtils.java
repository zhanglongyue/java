package priv.yue.sboot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    /**
     * json转换成对象
     */
    public static <T> T toObject(String jsonString, Class classz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (T) mapper.readValue(jsonString, classz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转换成json
     */
    public static String toJsonString(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
