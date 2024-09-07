package com.jobApplication.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jobApplication.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    ADMIN(createAdminPermissions()),
    MANAGER(createManagerPermissions()),
    CLIENT(createClientPermission());


    @Getter
    private final Set<Permission> permissions;
    private static Set<Permission> createAdminPermissions() {
        Set<Permission> permissions = new HashSet<>();
        Collections.addAll(permissions, ADMIN_READ, ADMIN_UPDATE, ADMIN_CREATE, ADMIN_DELETE,
                MANAGER_READ, MANAGER_UPDATE, MANAGER_CREATE, MANAGER_DELETE);
        return Collections.unmodifiableSet(permissions);
    }
    private static Set<Permission> createManagerPermissions() {
        Set<Permission> permissions = new HashSet<>();
        Collections.addAll(permissions,
                MANAGER_READ, MANAGER_UPDATE, MANAGER_CREATE, MANAGER_DELETE);
        return Collections.unmodifiableSet(permissions);
    }
    private static Set<Permission> createClientPermission() {
        Set<Permission> permissions = new HashSet<>();
        Collections.addAll(permissions,CLIENT_READ,CLIENT_UPDATE,CLIENT_CREATE,CLIENT_DELETE);
        return Collections.unmodifiableSet(permissions);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authority = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authority.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authority;

    }
}
