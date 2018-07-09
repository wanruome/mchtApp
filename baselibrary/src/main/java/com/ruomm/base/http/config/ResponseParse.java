package com.ruomm.base.http.config;

public interface ResponseParse {
    public Object parseResponseText(String resourceString, Class<?> cls);
}
