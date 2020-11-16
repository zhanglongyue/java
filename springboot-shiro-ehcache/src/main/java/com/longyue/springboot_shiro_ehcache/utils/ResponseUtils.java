package com.longyue.springboot_shiro_ehcache.utils;

import com.longyue.springboot_shiro_ehcache.vo.Response;

public class ResponseUtils {
    public static Response success(Object object) {
        Response result = new Response();
        result.setCode("0000");
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static Response success() {
        return success(null);
    }

    public static Response error(String code, String msg) {
        Response result = new Response();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
