package com.khoa_ly.backend_service.dto.request;

import com.khoa_ly.backend_service.dto.validator.RoleSubset;
import com.khoa_ly.backend_service.enumeration.Role;
import com.khoa_ly.backend_service.util.validation.PhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AccountRequest implements Serializable {

    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @PhoneNumber
    @NotBlank(message = "Phone must not be blank")
    private String phone;

    @NotNull(message = "Date of birth must not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthday;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotNull(message = "Role must not be null")
    @RoleSubset(anyOf = {Role.MANAGER, Role.STAFF})
    private Role role;

    @Valid
    @NotNull(message = "Contract must not be null")
    private ContractRequest contract;
}
