package com.spf.control.infrastructure.web.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

public class CustomHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> headers;

    public CustomHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.headers = new HashMap<>();
    }

    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> headerNames = Collections.list(super.getHeaderNames());
        headerNames.addAll(headers.keySet());
        return Collections.enumeration(headerNames);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> headerValues = new ArrayList<>();
        Collections.list(super.getHeaders(name)).forEach(headerValues::add);
        if (headers.containsKey(name)) {
            headerValues.add(headers.get(name));
        }
        return Collections.enumeration(headerValues);
    }
}
