package com.khoa_ly.backend_service.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.khoa_ly.backend_service.exception.ServiceException;
import com.khoa_ly.backend_service.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CloudinaryServiceImpl")
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadContractFile(String dest) {
        try {
            File file = new File(dest);

            String fileNameWithExtension = dest.substring(dest.lastIndexOf("/") + 1);
            String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf(".")) + "_" + LocalDateTime.now();

            String contractUrl = cloudinary.uploader()
                    .upload(file,
                            ObjectUtils.asMap(
                                    "folder", "EMPLOYEE_CONTRACT",
                                    "public_id", fileName))
                    .get("url")
                    .toString();

            log.info("Uploaded contract file to cloudinary: {}", contractUrl);

            return contractUrl;
        } catch (Exception e) {
            log.error("Error occurred while uploading image to cloudinary", e);
            throw new ServiceException("Error occurred while uploading image to cloudinary");
        }
    }
}
