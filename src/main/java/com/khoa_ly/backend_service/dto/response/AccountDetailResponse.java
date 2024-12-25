package com.khoa_ly.backend_service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDetailResponse extends AccountResponse implements Serializable {
    Date createdAt;
    Date updatedAt;
}
