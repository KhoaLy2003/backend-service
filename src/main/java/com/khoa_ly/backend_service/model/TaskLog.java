package com.khoa_ly.backend_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_task_log")
public class TaskLog extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "trigger_account")
    private Account triggerAccount;

    @Column(name = "action", columnDefinition = "TEXT")
    private String action;
}
