package org.film.service.impl;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.film.auth.util.RedisType.CLIENT_REFRESH_TOKEN;

@Service
public class ClientCacheToken implements org.film.service.Cache<Set<String>, String> {

    private final Cache cache;

    public ClientCacheToken(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(CLIENT_REFRESH_TOKEN.getValue());
    }

    @Override
    public void add(String key, String object) {
        Set<String> refreshTokens = cache.get(key, Set.class);
        if (refreshTokens == null) {
            refreshTokens = new HashSet<>();
        }
        refreshTokens.add(object);
        cache.put(key, refreshTokens);
    }

    @Override
    public Set<String> get(String key) {
        return Optional.ofNullable(cache.get(key, Set.class))
                .orElse(new HashSet<>());
    }

    @Override
    public void remove(String key, String value) {
        Set<String> refreshTokens = cache.get(key, Set.class);
        if (refreshTokens != null) {
            refreshTokens.remove(value);
            if (refreshTokens.isEmpty()) {
                cache.evict(key);
            } else {
                cache.put(key, refreshTokens);
            }
        }
    }
}