package com.khoa_ly.backend_service.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
public class ContractRequest implements Serializable {

    @NotNull(message = "Start date must not be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private Date startDate;

    @NotNull(message = "End date must not be null")
    @Future(message = "End date must be in the future")
    private Date endDate;

    @NotNull(message = "Salary must not be null")
    @Min(value = 0, message = "Salary must be greater than or equal to 0")
    private BigDecimal salary;
}
