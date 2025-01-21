package com.khoa_ly.backend_service.service;

import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.model.Contract;

public interface PdfService {
    String generateContractPdf(Account account, Contract contract);
}
