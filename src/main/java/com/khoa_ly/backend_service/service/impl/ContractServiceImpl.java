package com.khoa_ly.backend_service.service.impl;

import com.khoa_ly.backend_service.dto.request.ContractRequest;
import com.khoa_ly.backend_service.enumeration.ContractStatus;
import com.khoa_ly.backend_service.model.Account;
import com.khoa_ly.backend_service.model.Contract;
import com.khoa_ly.backend_service.repository.ContractRepository;
import com.khoa_ly.backend_service.service.CloudinaryService;
import com.khoa_ly.backend_service.service.ContractService;
import com.khoa_ly.backend_service.service.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ContractServiceImpl")
public class ContractServiceImpl implements ContractService {
    private final CloudinaryService cloudinaryService;
    private final ContractRepository contractRepository;
    private final PdfService pdfService;

    @Override
    public void createContract(Account account, ContractRequest contractRequest) {
        Contract contract = Contract
                .builder()
                .contractNumber("CONTRACT-" + account.getId())
                .account(account)
                .startDate(contractRequest.getStartDate())
                .endDate(contractRequest.getEndDate())
                .salary(contractRequest.getSalary())
                .status(ContractStatus.CREATED)
                .build();

        String dest = pdfService.generateContractPdf(account, contract);
        String contractFileUrl = cloudinaryService.uploadContractFile(dest);

        log.info("Generated contract PDF for account ID {} at {}", account.getId(), contractFileUrl);
        contract.setContractFileUrl(contractFileUrl);

        contractRepository.save(contract);
        log.info("Saved contract for account ID {}", account.getId());
    }
}
