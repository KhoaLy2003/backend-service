package com.khoa_ly.backend_service.service.impl;

import com.khoa_ly.backend_service.constant.Constant;
import com.khoa_ly.backend_service.dto.request.TaskRequest;
import com.khoa_ly.backend_service.dto.response.PageResponse;
import com.khoa_ly.backend_service.dto.response.TaskDetailResponse;
import com.khoa_ly.backend_service.dto.response.TaskResponse;
import com.khoa_ly.backend_service.enumeration.AccountStatus;
import com.khoa_ly.backend_service.enumeration.Role;
import com.khoa_ly.backend_service.enumeration.TaskStatus;
import com.khoa_ly.backend_service.exception.AccountInactiveException;
import com.khoa_ly.backend_service.exception.InvalidRoleException;
import com.khoa_ly.backend_service.exception.NotFoundException;
import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.model.Task;
import com.khoa_ly.backend_service.model.TaskLog;
import com.khoa_ly.backend_service.repository.AccountRepository;
import com.khoa_ly.backend_service.repository.TaskRepository;
import com.khoa_ly.backend_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "TaskServiceImpl")
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponse<TaskResponse> getTasks(int pageNo, int pageSize, TaskStatus status) {
        log.info("Fetching tasks - Page: {}, Size: {}, Status: {}", pageNo, pageSize, status);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> taskPage = (status == null)
                ? taskRepository.findAll(pageable)
                : taskRepository.findByStatus(status, pageable);

        log.info("Fetched {} tasks", taskPage.getContent().size());
        return PageResponse.<TaskResponse>builder()
                .currentPage(pageNo)
                .pageSize(pageSize)
                .totalPages(taskPage.getTotalPages())
                .totalElements(taskPage.getTotalElements())
                .data(taskPage.getContent()
                        .stream()
                        .map(account -> modelMapper.map(account, TaskResponse.class))
                        .toList())
                .build();
    }

    @Override
    public PageResponse<TaskResponse> getTasksByAccountId(String loginAccountEmail, int pageNo, int pageSize, TaskStatus status) {
        Account assignedAccount = accountRepository.findByEmail(loginAccountEmail).orElseThrow(() -> new NotFoundException("Account not found"));
        log.info("Fetching tasks - Page: {}, Size: {}, Status: {} for account {}", pageNo, pageSize, status, loginAccountEmail);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> taskPage = (status == null)
                ? taskRepository.findByAssignedAccount(assignedAccount, pageable)
                : taskRepository.findByAssignedAccountAndStatus(assignedAccount, status, pageable);

        log.info("Fetched {} tasks", taskPage.getContent().size());
        return PageResponse.<TaskResponse>builder()
                .currentPage(pageNo)
                .pageSize(pageSize)
                .totalPages(taskPage.getTotalPages())
                .totalElements(taskPage.getTotalElements())
                .data(taskPage.getContent()
                        .stream()
                        .map(account -> modelMapper.map(account, TaskResponse.class))
                        .toList())
                .build();
    }

    @Override
    public TaskDetailResponse getTaskDetail(long taskId) {
        log.info("Fetching details for task ID {}", taskId);
        Task task = getTaskById(taskId);
        return modelMapper.map(task, TaskDetailResponse.class);
    }

    @Override
    public long createTask(String accountCreateEmail, TaskRequest taskRequest) {
        Account createdAccount = accountRepository.findByEmail(accountCreateEmail).orElseThrow(() -> new NotFoundException("Account not found"));
        Account assignedAccount = accountRepository.findById(taskRequest.getAssignedAccountId()).orElseThrow(() -> new NotFoundException("Account not found"));
        if (assignedAccount.getStatus().equals(AccountStatus.INACTIVE)) {
            log.error("Account create task is inactive");
            throw new AccountInactiveException("Account is inactive");
        }

        if (!assignedAccount.getRole().equals(Role.STAFF)) {
            log.error("Account assigned task is not staff role");
            throw new InvalidRoleException("Account assigned task is not staff role");
        }

        Task task = Task
                .builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .note(taskRequest.getNote())
                .createdAccount(createdAccount)
                .assignedAccount(assignedAccount)
                .status(TaskStatus.CREATED)
                .build();

        TaskLog taskLog = TaskLog
                .builder()
                .task(task)
                .triggerAccount(createdAccount)
                .action(String.format(Constant.TASK_CREATION, task.getTitle(), assignedAccount.getLastName() + " " + assignedAccount.getFirstName()))
                .build();
        task.setTaskLogs(new ArrayList<>(List.of(taskLog)));

        taskRepository.save(task);
        log.info("Created task with id {}", task.getId());
        return task.getId();
    }

    @Transactional
    @Override
    public void updateTask(long taskId, TaskRequest taskRequest) {
        log.info("Updating task with ID {}", taskId);
        Task task = getTaskById(taskId);

        Account createdAccount = accountRepository.findByEmail(task.getCreatedAccount().getEmail()).orElseThrow(() -> new NotFoundException("Account not found"));
        Account assignedAccount = accountRepository.findById(taskRequest.getAssignedAccountId()).orElseThrow(() -> new NotFoundException("Account not found"));
        if (assignedAccount.getStatus().equals(AccountStatus.INACTIVE)) {
            log.error("Account assigned task is inactive");
            throw new AccountInactiveException("Account is inactive");
        }

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setNote(taskRequest.getNote());
        task.setAssignedAccount(assignedAccount);
        TaskLog taskLog = TaskLog
                .builder()
                .task(task)
                .triggerAccount(createdAccount)
                .action(String.format(Constant.TASK_UPDATE, task.getTitle(), assignedAccount.getLastName() + " " + assignedAccount.getFirstName()))
                .build();
        task.getTaskLogs().add(taskLog);

        taskRepository.save(task);
        log.info("Updated task with ID {}", taskId);
    }

    @Transactional
    @Override
    public void deleteTask(long taskId) {
//        Task task = getTaskById(taskId);
//        if (!task.getStatus().equals(TaskStatus.CREATED)) {
//            log.error("Task with ID {} cannot be deleted because its status is {}", taskId, task.getStatus());
//            throw new InvalidTaskStateException("Task can only be deleted when its status is CREATED");
//        }

        taskRepository.deleteById(taskId);
        log.info("Deleted task with ID {}", taskId);
    }

    @Override
    public void changeTaskStatus(String triggerAccountEmail, long taskId, TaskStatus status) {
        log.info("Changing status for task ID {} to {}", taskId, status);
        Account triggerAccount = accountRepository.findByEmail(triggerAccountEmail).orElseThrow(() -> new NotFoundException("Account not found"));
        Task task = getTaskById(taskId);
        task.setStatus(status);

        TaskLog taskLog = TaskLog
                .builder()
                .task(task)
                .triggerAccount(triggerAccount)
                .action(String.format(Constant.TASK_CHANGE_STATUS, task.getTitle(), status, triggerAccount.getLastName() + " " + triggerAccount.getFirstName()))
                .build();
        task.getTaskLogs().add(taskLog);
        taskRepository.save(task);
        log.info("Changed status of task with ID {} to {}", taskId, status);
    }

    private Task getTaskById(long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Task with ID {} not found", taskId);
            return new NotFoundException("Task not found");
        });
    }
}
