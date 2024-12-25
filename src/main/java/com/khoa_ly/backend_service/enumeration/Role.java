package com.khoa_ly.backend_service.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.khoa_ly.backend_service.enumeration.Permission.CHANGE_ACCOUNT_STATUS;
import static com.khoa_ly.backend_service.enumeration.Permission.CHANGE_TASK_STATUS;
import static com.khoa_ly.backend_service.enumeration.Permission.CREATE_ACCOUNT;
import static com.khoa_ly.backend_service.enumeration.Permission.CREATE_TASK;
import static com.khoa_ly.backend_service.enumeration.Permission.DELETE_ACCOUNT;
import static com.khoa_ly.backend_service.enumeration.Permission.DELETE_TASK;
import static com.khoa_ly.backend_service.enumeration.Permission.UPDATE_ACCOUNT;
import static com.khoa_ly.backend_service.enumeration.Permission.UPDATE_TASK;
import static com.khoa_ly.backend_service.enumeration.Permission.VIEW_ACCOUNT;
import static com.khoa_ly.backend_service.enumeration.Permission.VIEW_TASK;


@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    VIEW_ACCOUNT,
                    CREATE_ACCOUNT,
                    UPDATE_ACCOUNT,
                    DELETE_ACCOUNT,
                    CHANGE_ACCOUNT_STATUS
            )
    ),
    MANAGER(
            Set.of(
                    VIEW_ACCOUNT,
                    VIEW_TASK,
                    CREATE_TASK,
                    UPDATE_TASK,
                    DELETE_TASK,
                    CHANGE_TASK_STATUS
            )
    ),
    STAFF(
            Set.of(
                    VIEW_TASK,
                    CHANGE_TASK_STATUS
            )
    ),
    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
