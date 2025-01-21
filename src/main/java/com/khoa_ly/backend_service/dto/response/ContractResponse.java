package com.khoa_ly.backend_service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractResponse implements Serializable {
    Long id;
    String contractNumber;
    String startDate;
    String endDate;
    Double salary;
    String status;
    String contractFileUrl;
}
