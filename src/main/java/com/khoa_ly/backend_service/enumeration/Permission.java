package com.khoa_ly.backend_service.enumeration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    VIEW_ACCOUNT,
    CREATE_ACCOUNT,
    UPDATE_ACCOUNT,
    DELETE_ACCOUNT,
    CHANGE_ACCOUNT_STATUS,
    VIEW_TASK,
    CREATE_TASK,
    UPDATE_TASK,
    DELETE_TASK,
    CHANGE_TASK_STATUS,
}
