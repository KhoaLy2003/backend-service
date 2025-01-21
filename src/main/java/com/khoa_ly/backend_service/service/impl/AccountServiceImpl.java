package com.khoa_ly.backend_service.service.impl;

import com.khoa_ly.backend_service.constant.Constant;
import com.khoa_ly.backend_service.dto.request.AccountRequest;
import com.khoa_ly.backend_service.dto.response.AccountDetailResponse;
import com.khoa_ly.backend_service.dto.response.AccountResponse;
import com.khoa_ly.backend_service.dto.response.ContractResponse;
import com.khoa_ly.backend_service.dto.response.PageResponse;
import com.khoa_ly.backend_service.enumeration.AccountStatus;
import com.khoa_ly.backend_service.exception.DuplicateEmailException;
import com.khoa_ly.backend_service.exception.NotFoundException;
import com.khoa_ly.backend_service.exception.ServiceException;
import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.repository.AccountRepository;
import com.khoa_ly.backend_service.service.AccountService;
import com.khoa_ly.backend_service.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AccountServiceImpl")
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ContractService contractService;

    @Override
    public PageResponse<AccountResponse> getAccounts(int pageNo, int pageSize, AccountStatus status) {
        log.info("Fetching accounts - Page: {}, Size: {}, Status: {}", pageNo, pageSize, status);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Account> accountPage = (status == null)
                ? accountRepository.findAll(pageable)
                : accountRepository.findByStatus(status, pageable);

        log.info("Fetched {} accounts", accountPage.getContent().size());
        return PageResponse.<AccountResponse>builder()
                .currentPage(pageNo)
                .pageSize(pageSize)
                .totalPages(accountPage.getTotalPages())
                .totalElements(accountPage.getTotalElements())
                .data(accountPage.getContent()
                        .stream()
                        .map(account -> modelMapper.map(account, AccountResponse.class))
                        .toList())
                .build();
    }

    @Override
    public AccountDetailResponse getAccountDetail(long accountId) {
        log.info("Fetching details for account ID {}", accountId);
        Account account = getAccountById(accountId);

        log.info(account.getContracts().stream().map(contract -> modelMapper.map(contract, ContractResponse.class)).toList().toString());

        AccountDetailResponse accountDetailResponse = modelMapper.map(account, AccountDetailResponse.class);
        List<ContractResponse> contracts = account.getContracts().stream()
                .map(contract -> modelMapper.map(contract, ContractResponse.class))
                .toList();
        accountDetailResponse.setContracts(contracts);
        return accountDetailResponse;
    }

    @Override
    @Transactional
    public long createAccount(AccountRequest accountRequest) {
        checkEmailDuplication(accountRequest.getEmail());

        try {
            Account account = Account
                    .builder()
                    .firstName(accountRequest.getFirstName())
                    .lastName(accountRequest.getLastName())
                    .email(accountRequest.getEmail())
                    .phone(accountRequest.getPhone())
                    .birthday(accountRequest.getBirthday())
                    .role(accountRequest.getRole())
                    .address(accountRequest.getAddress())
                    .status(AccountStatus.INACTIVE)
                    .password(passwordEncoder.encode(Constant.DEFAULT_PASSWORD))
                    .build();
            accountRepository.save(account);
            log.info("Created account with id {}", account.getId());

            contractService.createContract(account, accountRequest.getContract());

            return account.getId();
        } catch (Exception e) {
            log.error("Error creating account", e);
            throw new ServiceException("Error creating account");
        }
    }

    @Override
    public void updateAccount(long accountId, AccountRequest accountRequest) {
        log.info("Updating account with ID {}", accountId);
        Account account = getAccountById(accountId);

        if (!account.getEmail().equals(accountRequest.getEmail())) {
            checkEmailDuplication(accountRequest.getEmail());
        }

        account.setFirstName(accountRequest.getFirstName());
        account.setLastName(accountRequest.getLastName());
        account.setEmail(accountRequest.getEmail());
        account.setPhone(accountRequest.getPhone());
        account.setBirthday(accountRequest.getBirthday());

        accountRepository.save(account);
        log.info("Updated account with ID {}", accountId);
    }

    @Override
    public void deleteAccount(long accountId) {
        accountRepository.deleteById(accountId);
        log.info("Deleted account with ID {}", accountId);
    }

    @Override
    public void changeAccountStatus(long accountId, AccountStatus status) {
        log.info("Changing status for account ID {} to {}", accountId, status);
        Account account = getAccountById(accountId);
        account.setStatus(status);
        accountRepository.save(account);
        log.info("Changed status for account ID {} to {}", accountId, status);
    }

    private Account getAccountById(long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> {
            log.error("Account with ID {} not found", accountId);
            return new NotFoundException("Account not found");
        });
    }

    private void checkEmailDuplication(String email) {
        if (accountRepository.existsByEmail(email)) {
            log.error("Account already exists with email {}", email);
            throw new DuplicateEmailException("Email already exists");
        }
    }
}
