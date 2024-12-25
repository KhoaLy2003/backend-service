package com.khoa_ly.backend_service.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public void initialize(PhoneNumber phoneNumberNo) {

    }

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext cxt) {
        if (phoneNo == null) {
            return false;
        }
        return phoneNo.matches("(03|05|07|08|09|01[2689])\\d{8}");
    }
}
