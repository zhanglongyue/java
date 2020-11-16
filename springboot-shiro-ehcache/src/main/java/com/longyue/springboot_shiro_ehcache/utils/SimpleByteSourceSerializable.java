package com.longyue.springboot_shiro_ehcache.utils;

import org.apache.shiro.util.SimpleByteSource;
import java.io.Serializable;

public class SimpleByteSourceSerializable extends SimpleByteSource implements Serializable {
    public SimpleByteSourceSerializable(String string) {
        super(string);
    }
}
