package com.example.practicekeycloak.user.application.service;

import com.example.practicekeycloak.user.infrastructure.keycloak.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakProperties.class)
public class UserUpdateService {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;

    // 비밀번호 변경
    public void updatePassword(UUID userId, String password) {
        CredentialRepresentation passwordCredential = new CredentialRepresentation();
        passwordCredential.setTemporary(false);
        passwordCredential.setType(CredentialRepresentation.PASSWORD);
        passwordCredential.setValue(password);

        keycloak.realm(properties.getRealm()).users().get(userId.toString()).resetPassword(passwordCredential);
    }

    // Role 변경
    public void updateUserRole(UUID userId, List<String> roleNames) {
        String id = userId.toString();
        String realm = properties.getRealm();
        RoleScopeResource resource = keycloak.realm(realm).users().get(id).roles().realmLevel();

        // 기존 Role 제거
        resource.remove(resource.listAll());

        // 새 Role 추가
        List<RoleRepresentation> newRoles = roleNames.stream().map(roleName -> keycloak.realm(realm).roles().get(roleName).toRepresentation()).toList();
        resource.add(newRoles);
    }
}
