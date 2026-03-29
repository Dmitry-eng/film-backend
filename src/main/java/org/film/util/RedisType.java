package org.film.auth.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisType {
    CLIENT_REFRESH_TOKEN("client-refresh-token-storage");

    private final String value;
}