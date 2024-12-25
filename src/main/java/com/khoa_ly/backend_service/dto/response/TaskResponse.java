package com.khoa_ly.backend_service.dto.response;

import com.khoa_ly.backend_service.enumeration.TaskStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse implements Serializable {
    Long id;
    String title;
    String description;
    String note;
    TaskStatus status;
}
