package com.khoa_ly.backend_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest implements Serializable {
    String email;
    String password;
}
