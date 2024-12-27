package com.khoa_ly.backend_service.service;

public interface RedisService {
    void set(String key, String value, long expiration);
    Object get(String key);
    void delete(String key);
    boolean hasKey(String key);
}
