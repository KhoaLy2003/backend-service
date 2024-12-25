package com.khoa_ly.backend_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public ExceptionResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
