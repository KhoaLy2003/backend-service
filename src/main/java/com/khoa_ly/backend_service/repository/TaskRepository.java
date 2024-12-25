package com.khoa_ly.backend_service.repository;

import com.khoa_ly.backend_service.enumeration.TaskStatus;
import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByAssignedAccount(Account assignedAccount, Pageable pageable);

    Page<Task> findByAssignedAccountAndStatus(Account assignedAccount, TaskStatus status, Pageable pageable);
}
