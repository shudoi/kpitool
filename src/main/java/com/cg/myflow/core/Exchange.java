package com.cg.myflow.core;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.jsonwebtoken.Claims;

public class Exchange implements Serializable {

	@JsonProperty("body")
	private Object body;
	@JsonProperty("header")
	private Map<String, Object> header = new LinkedHashMap<>();
	@JsonIgnore
	private Claims claims;
	@JsonIgnore
	private Map<String, Object> rawData = new LinkedHashMap<>();
	@JsonIgnore
	private Map<String, Object> model;
	@JsonIgnore
	private String page;
	@JsonIgnore
	private boolean produceEnd = false;

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

	public Object getRawData(String key) {
		return this.rawData.get(key);
	}

	public void setRawData(String key, Object value) {
		this.rawData.put(key, value);
	}

	public Map<String, Object> getModel() {
		return this.model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;

	}

	public String getPage() {
		return this.page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public boolean isProduceEnd() {
		return this.produceEnd;
	}

	public void setProduceEnd(boolean produceEnd) {
		this.produceEnd = produceEnd;
	}

}
