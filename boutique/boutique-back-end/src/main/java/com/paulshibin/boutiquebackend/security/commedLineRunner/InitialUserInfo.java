package com.paulshibin.boutiquebackend.security.commedLineRunner;

import com.paulshibin.boutiquebackend.security.entity.UserInfoEntity;
import com.paulshibin.boutiquebackend.security.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class InitialUserInfo implements CommandLineRunner {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        UserInfoEntity manager = UserInfoEntity.builder()
                .username("Manager")
                .password(passwordEncoder.encode("password"))
                .roles("ROLE_MANAGER")
                .emailId("manager@manager.com")
                .build();
        UserInfoEntity admin = UserInfoEntity.builder()
                .username("Admin")
                .password(passwordEncoder.encode("password"))
                .roles("ROLE_ADMIN")
                .emailId("admin@admin.com")
                .build();
        UserInfoEntity user = UserInfoEntity.builder()
                .username("User")
                .password(passwordEncoder.encode("password"))
                .roles("ROLE_USER")
                .emailId("user@user.com")
                .build();
        userInfoRepository.saveAll(List.of(manager, admin, user));
    }
}
