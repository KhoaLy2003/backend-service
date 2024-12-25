package com.khoa_ly.backend_service.service;

import com.khoa_ly.backend_service.dto.request.AccountRequest;
import com.khoa_ly.backend_service.dto.response.AccountDetailResponse;
import com.khoa_ly.backend_service.dto.response.AccountResponse;
import com.khoa_ly.backend_service.dto.response.PageResponse;
import com.khoa_ly.backend_service.enumeration.AccountStatus;

public interface AccountService {
    PageResponse<AccountResponse> getAccounts(int pageNo, int pageSize, AccountStatus status);

    AccountDetailResponse getAccountDetail(long accountId);

    long createAccount(AccountRequest accountRequest);

    void updateAccount(long accountId, AccountRequest accountRequest);

    void deleteAccount(long accountId);

    void changeAccountStatus(long accountId, AccountStatus status);
}
