package com.khoa_ly.backend_service.dto.response;

import com.khoa_ly.backend_service.enumeration.AccountStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse implements Serializable {
    Long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    Date birthday;
    AccountStatus status;
}
