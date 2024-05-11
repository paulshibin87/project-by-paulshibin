package com.paulshibin.boutiquebackend.security.service;

import com.paulshibin.boutiquebackend.security.config.jwtConfig.JwtTokenGeneratorService;
import com.paulshibin.boutiquebackend.security.dto.AuthResponseDto;
import com.paulshibin.boutiquebackend.security.dto.TokenType;
import com.paulshibin.boutiquebackend.security.dto.UserRegistrationDto;
import com.paulshibin.boutiquebackend.security.entity.RefreshTokenEntity;
import com.paulshibin.boutiquebackend.security.entity.UserInfoEntity;
import com.paulshibin.boutiquebackend.security.mapper.UserInfoMapper;
import com.paulshibin.boutiquebackend.security.repository.RefreshTokenRepository;
import com.paulshibin.boutiquebackend.security.repository.UserInfoRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserInfoRepository userInfoRepository;
    private final JwtTokenGeneratorService jwtTokenGeneratorService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserInfoMapper userInfoMapper;

    private static Authentication createAuthenticationObject(UserInfoEntity userInfoEntity) {
        String username = userInfoEntity.getEmailId();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);
        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDto getJwtTokenAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            UserInfoEntity userInfoEntity = userInfoRepository
                    .findByEmailId(authentication.getName())
                    .orElseThrow(() -> {
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
                    });
            String accessToken = jwtTokenGeneratorService.generateAccessToken(authentication);
            String refreshToken = jwtTokenGeneratorService.generateRefreshToken(authentication);
            saveUserRefreshToken(userInfoEntity, refreshToken);
            createRefreshTokenCookie(response, refreshToken);
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(userInfoEntity.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    private Cookie createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    private void saveUserRefreshToken(UserInfoEntity userInfoEntity, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));
        UserInfoEntity userInfoEntity = refreshTokenEntity.getUser();

        Authentication authentication = createAuthenticationObject(userInfoEntity);
        String accessToken = jwtTokenGeneratorService.generateAccessToken(authentication);
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(userInfoEntity.getUsername())
                .tokenType(TokenType.Bearer)
                .build();
    }

    public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto, HttpServletResponse response) {
        try {
            Optional<UserInfoEntity> userInfoEntity = userInfoRepository.findByEmailId(userRegistrationDto.userEmail());
            if (userInfoEntity.isPresent()) {
                throw new Exception("User Already Exist");
            }

            UserInfoEntity userDetailsEntity = userInfoMapper.convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);

            String accessToken = jwtTokenGeneratorService.generateAccessToken(authentication);
            String refreshToken = jwtTokenGeneratorService.generateRefreshToken(authentication);

            UserInfoEntity saveUserDetails = userInfoRepository.save(userDetailsEntity);
            saveUserRefreshToken(userDetailsEntity, refreshToken);

            createRefreshTokenCookie(response, refreshToken);

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(saveUserDetails.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
