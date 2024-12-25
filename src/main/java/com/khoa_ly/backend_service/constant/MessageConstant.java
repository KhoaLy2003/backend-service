package com.khoa_ly.backend_service.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstant {

    /**
     * Account message
     */
    public static final String GET_ACCOUNT_LIST_SUCCESSFULLY = "account.get_account_list_successfully";
    public static final String GET_ACCOUNT_DETAIL_SUCCESSFULLY = "account.get_account_detail_successfully";
    public static final String CREATE_ACCOUNT_SUCCESSFULLY = "account.create_account_successfully";
    public static final String UPDATE_ACCOUNT_SUCCESSFULLY = "account.update_account_successfully";
    public static final String CHANGE_ACCOUNT_STATUS_SUCCESSFULLY = "account.change_account_status_successfully";
    public static final String DELETE_ACCOUNT_SUCCESSFULLY = "account.delete_account_successfully";

    /**
     * Task message
     */
    public static final String GET_TASK_LIST_SUCCESSFULLY = "task.get_task_list_successfully";
    public static final String GET_TASK_DETAIL_SUCCESSFULLY = "task.get_task_detail_successfully";
    public static final String CREATE_TASK_SUCCESSFULLY = "task.create_task_successfully";
    public static final String UPDATE_TASK_SUCCESSFULLY = "task.update_task_successfully";
    public static final String CHANGE_TASK_STATUS_SUCCESSFULLY = "task.change_task_status_successfully";
    public static final String DELETE_TASK_SUCCESSFULLY = "task.delete_task_successfully";
}
