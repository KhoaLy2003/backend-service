package com.khoa_ly.backend_service.service;

import com.khoa_ly.backend_service.dto.request.ContractRequest;
import com.khoa_ly.backend_service.model.Account;

public interface ContractService {
    void createContract(Account account, ContractRequest contractRequest);
}
