package com.khoa_ly.backend_service.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final String DEFAULT_PASSWORD = "12345";
    public static final String TASK_CREATION = "Task created with title '%s' and assigned to '%s'";
    public static final String TASK_UPDATE = "Task updated with title '%s' and assigned to '%s'";
    public static final String TASK_CHANGE_STATUS = "Task with title '%s' status changed to '%s' by '%s'";

    public static final String REFRESH_TOKEN_REDIS_PREFIX_KEY = "refresh-token:";
}
