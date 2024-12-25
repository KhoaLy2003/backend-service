package com.khoa_ly.backend_service.controller;

import com.khoa_ly.backend_service.constant.MessageConstant;
import com.khoa_ly.backend_service.dto.request.TaskRequest;
import com.khoa_ly.backend_service.dto.response.PageResponse;
import com.khoa_ly.backend_service.dto.response.ResponseData;
import com.khoa_ly.backend_service.dto.response.TaskDetailResponse;
import com.khoa_ly.backend_service.dto.response.TaskResponse;
import com.khoa_ly.backend_service.enumeration.TaskStatus;
import com.khoa_ly.backend_service.service.JwtService;
import com.khoa_ly.backend_service.service.TaskService;
import com.khoa_ly.backend_service.util.message.MessageLocalization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/tasks")
@Slf4j(topic = "TaskController")
@RequiredArgsConstructor
@Tag(name = "Task Controller")
public class TaskController {
    private final MessageLocalization messageLocalization;
    private final TaskService taskService;
    private final JwtService jwtService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_TASK')")
    @Operation(summary = "Get task list", description = "API retrieve task records from database")
    public ResponseData<PageResponse<TaskResponse>> getTasks(@RequestParam(required = false) TaskStatus status,
                                                             @RequestParam(required = false, defaultValue = "0") int pageNo,
                                                             @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), messageLocalization.getLocalizedMessage(MessageConstant.GET_TASK_LIST_SUCCESSFULLY), taskService.getTasks(pageNo, pageSize, status));
    }

    @GetMapping("/relative")
    @PreAuthorize("hasAnyAuthority('VIEW_TASK')")
    @Operation(summary = "Get task list by account", description = "API retrieve task records of account login from database")
    public ResponseData<PageResponse<TaskResponse>> getRelativeTasks(@RequestParam(required = false) TaskStatus status,
                                                                     @RequestParam(required = false, defaultValue = "0") int pageNo,
                                                                     @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                                     Principal principal) {
        return new ResponseData<>(HttpStatus.OK.value(), messageLocalization.getLocalizedMessage(MessageConstant.GET_TASK_LIST_SUCCESSFULLY), taskService.getTasksByAccountId(principal.getName(), pageNo, pageSize, status));
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('VIEW_TASK')")
    @Operation(summary = "Get task detail", description = "API retrieve task detail from database")
    public ResponseData<TaskDetailResponse> getTask(@PathVariable long taskId) {
        log.info("Get task with id {}", taskId);
        TaskDetailResponse taskDetailResponse = taskService.getTaskDetail(taskId);
        return new ResponseData<>(HttpStatus.OK.value(), messageLocalization.getLocalizedMessage(MessageConstant.GET_TASK_DETAIL_SUCCESSFULLY), taskDetailResponse);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CREATE_TASK')")
    @Operation(summary = "Create new task", description = "API add new task record to database")
    public ResponseData<Long> createTask(@Valid @RequestBody TaskRequest taskRequest, Principal principal) {
        log.info("Account create: {}", principal.getName());
        log.info("Create task: {}", taskRequest.toString());
        long taskId = taskService.createTask(principal.getName(), taskRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(), messageLocalization.getLocalizedMessage(MessageConstant.CREATE_TASK_SUCCESSFULLY), taskId);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_TASK')")
    @Operation(summary = "Update task", description = "API update task to database")
    public ResponseData<TaskResponse> updateTask(@PathVariable long taskId, @Valid @RequestBody TaskRequest taskRequest) {
        log.info("Update task with id {}", taskId);
        taskService.updateTask(taskId, taskRequest);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), messageLocalization.getLocalizedMessage(MessageConstant.UPDATE_TASK_SUCCESSFULLY));
    }

    @PatchMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('CHANGE_TASK_STATUS')")
    @Operation(summary = "Change task status", description = "API change task status to database")
    public ResponseData<TaskResponse> changeTaskStatus(@PathVariable long taskId, @Valid @RequestParam TaskStatus status, Principal principal) {
        log.info("Change task with id {} to status {}", taskId, status);
        taskService.changeTaskStatus(principal.getName(), taskId, status);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), messageLocalization.getLocalizedMessage(MessageConstant.CHANGE_TASK_STATUS_SUCCESSFULLY));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('DELETE_TASK')")
    @Operation(summary = "Delete account", description = "API delete account record from database")
    public ResponseData<Object> deleteAccount(@PathVariable long taskId) {
        log.info("Delete task with id {}", taskId);
        taskService.deleteTask(taskId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), messageLocalization.getLocalizedMessage(MessageConstant.DELETE_TASK_SUCCESSFULLY));
    }
}
