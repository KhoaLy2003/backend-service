package com.khoa_ly.backend_service.controller;

import com.khoa_ly.backend_service.constant.MessageConstant;
import com.khoa_ly.backend_service.dto.request.AccountRequest;
import com.khoa_ly.backend_service.dto.response.AccountResponse;
import com.khoa_ly.backend_service.dto.response.PageResponse;
import com.khoa_ly.backend_service.dto.response.ResponseData;
import com.khoa_ly.backend_service.enumeration.AccountStatus;
import com.khoa_ly.backend_service.service.AccountService;
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

@RestController
@RequestMapping("/accounts")
@Slf4j(topic = "AccountController")
@RequiredArgsConstructor
@Tag(name = "Account Controller")
public class AccountController {

    private final MessageLocalization messageLocalization;
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_ACCOUNT')")
    @Operation(summary = "Get account list", description = "API retrieve account records from database")
    public ResponseData<PageResponse<AccountResponse>> getAccounts(@RequestParam(required = false) AccountStatus status,
                                                                   @RequestParam(required = false, defaultValue = "0") int pageNo,
                                                                   @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return new ResponseData<>(HttpStatus.OK.value(), messageLocalization.getLocalizedMessage(MessageConstant.GET_ACCOUNT_LIST_SUCCESSFULLY), accountService.getAccounts(pageNo, pageSize, status));
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasAnyAuthority('VIEW_ACCOUNT')")
    @Operation(summary = "Get account detail", description = "API retrieve account detail from database")
    public ResponseData<AccountResponse> getAccount(@PathVariable long accountId) {
        log.info("Get account with id {}", accountId);
        return new ResponseData<>(HttpStatus.OK.value(), messageLocalization.getLocalizedMessage(MessageConstant.GET_ACCOUNT_DETAIL_SUCCESSFULLY), accountService.getAccountDetail(accountId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CREATE_ACCOUNT')")
    @Operation(summary = "Create new account", description = "API add new account record to database")
    public ResponseData<Long> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        log.info("Create account: {}", accountRequest.toString());

        long accountId = accountService.createAccount(accountRequest);

        return new ResponseData<>(HttpStatus.CREATED.value(), messageLocalization.getLocalizedMessage(MessageConstant.CREATE_ACCOUNT_SUCCESSFULLY), accountId);
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_ACCOUNT')")
    @Operation(summary = "Update account", description = "API update account to database")
    public ResponseData<AccountResponse> updateAccount(@PathVariable long accountId, @Valid @RequestBody AccountRequest accountRequest) {
        log.info("Update account with id {}", accountId);
        accountService.updateAccount(accountId, accountRequest);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), messageLocalization.getLocalizedMessage(MessageConstant.UPDATE_ACCOUNT_SUCCESSFULLY));
    }

    @PatchMapping("/{accountId}")
    @PreAuthorize("hasAnyAuthority('CHANGE_ACCOUNT_STATUS')")
    @Operation(summary = "Change account status", description = "API change account status to database")
    public ResponseData<AccountResponse> changeAccountStatus(@PathVariable long accountId, @Valid @RequestParam AccountStatus status) {
        log.info("Change account with id {} to status {}", accountId, status);
        accountService.changeAccountStatus(accountId, status);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), messageLocalization.getLocalizedMessage(MessageConstant.CHANGE_ACCOUNT_STATUS_SUCCESSFULLY));
    }

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasAnyAuthority('DELETE_ACCOUNT')")
    @Operation(summary = "Delete account", description = "API delete account record from database")
    public ResponseData<Object> deleteAccount(@PathVariable long accountId) {
        log.info("Delete account with id {}", accountId);
        accountService.deleteAccount(accountId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), messageLocalization.getLocalizedMessage(MessageConstant.DELETE_ACCOUNT_SUCCESSFULLY));
    }
}
