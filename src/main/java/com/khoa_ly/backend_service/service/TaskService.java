package com.khoa_ly.backend_service.service;

import com.khoa_ly.backend_service.dto.request.TaskRequest;
import com.khoa_ly.backend_service.dto.response.PageResponse;
import com.khoa_ly.backend_service.dto.response.TaskDetailResponse;
import com.khoa_ly.backend_service.dto.response.TaskResponse;
import com.khoa_ly.backend_service.enumeration.TaskStatus;

public interface TaskService {
    PageResponse<TaskResponse> getTasks(int pageNo, int pageSize, TaskStatus status);

    PageResponse<TaskResponse> getTasksByAccountId(String loginAccountEmail, int pageNo, int pageSize, TaskStatus status);

    TaskDetailResponse getTaskDetail(long taskId);

    long createTask(String accountCreateEmail, TaskRequest taskRequest);

    void updateTask(long taskId, TaskRequest taskRequest);

    void deleteTask(long taskId);

    void changeTaskStatus(String triggerAccountEmail, long taskId, TaskStatus status);
}
