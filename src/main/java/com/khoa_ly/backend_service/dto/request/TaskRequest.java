package com.khoa_ly.backend_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequest implements Serializable {

    @NotBlank(message = "Title must not be blank")
    String title;

    @NotBlank(message = "Description must not be blank")
    String description;

    String note;

    @NotNull(message = "Assigned account id mut not be null")
    Long assignedAccountId;
}
