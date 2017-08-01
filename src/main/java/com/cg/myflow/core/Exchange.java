package com.cg.myflow.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Exchange implements Serializable {

    @JsonProperty("body")
    private Object body;
    @JsonProperty("header")
    private Map<String, Object> header = new LinkedHashMap<>();
    @JsonIgnore
    private Claims claims;

    public void setBody(Object body) {
        this.body = body;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public void setHeader(String key, Object value) {
        this.header.put(key, value);
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public Object getBody() {
        return this.body;
    }

    public Map<String, Object> getHeaders() {
        return this.header;
    }

    public Object getHeader(String key) {
        return this.header.get(key);
    }

    public <T> T getHeader(String key, Class<T> clazz) {
        return clazz.cast(this.header.get(key));
    }

    public Claims getClaims() {
        return this.claims;
    }

}
