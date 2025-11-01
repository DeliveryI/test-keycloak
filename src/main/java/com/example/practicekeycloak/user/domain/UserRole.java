package com.example.practicekeycloak.user.domain;

public enum UserRole {
    CUSTOMER, OWNER, MANAGER, MASTER;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
