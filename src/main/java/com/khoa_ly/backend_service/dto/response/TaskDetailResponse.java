package com.khoa_ly.backend_service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDetailResponse extends TaskResponse implements Serializable {
    Date createdAt;
    Date updatedAt;

    CreatedAccountResponse createdAccount;
    AssignedAccountResponse assignedAccount;
    List<TaskLogResponse> taskLogs;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CreatedAccountResponse implements Serializable {
        Long createdAccountId;
        String createdAccountFirstName;
        String createdAccountLastName;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AssignedAccountResponse implements Serializable {
        Long assignedAccountId;
        String assignedAccountFirstName;
        String assignedAccountLastName;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TaskLogResponse implements Serializable {
        Long taskLogId;
        String action;
        TriggerAccountResponse triggerAccount;
        Date createdAt;
        Date updatedAt;

        @Getter
        @Setter
        @FieldDefaults(level = AccessLevel.PRIVATE)
        public static class TriggerAccountResponse implements Serializable {
            Long triggerAccountId;
            String triggerAccountFirstName;
            String triggerAccountLastName;
        }
    }
}
