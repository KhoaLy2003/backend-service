package com.khoa_ly.backend_service.dto.validator;

import com.khoa_ly.backend_service.enumeration.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class RoleSubSetValidator implements ConstraintValidator<RoleSubset, Role> {
    private Role[] roles;

    @Override
    public void initialize(RoleSubset constraint) {
        this.roles = constraint.anyOf();
    }

    @Override
    public boolean isValid(Role value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || Arrays.asList(roles).contains(value);
    }
}
