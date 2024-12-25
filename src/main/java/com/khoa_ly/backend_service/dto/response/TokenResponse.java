package com.khoa_ly.backend_service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse implements Serializable {
    String accessToken;
    String refreshToken;
}
