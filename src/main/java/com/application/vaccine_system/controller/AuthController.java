package com.application.vaccine_system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.vaccine_system.annotation.ApiMessage;
import com.application.vaccine_system.config.security.SecurityUtil;
import com.application.vaccine_system.exception.InvalidException;
import com.application.vaccine_system.model.User;
import com.application.vaccine_system.model.request.ReqLogin;
import com.application.vaccine_system.model.request.ReqRegister;
import com.application.vaccine_system.model.response.Pagination;
import com.application.vaccine_system.model.response.ResLogin;
import com.application.vaccine_system.service.UserService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${mhieu.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/login")
    @ApiMessage("Login successful")
    public ResponseEntity<ResLogin> login(@Valid @RequestBody ReqLogin reqLogin) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                reqLogin.getUsername(), reqLogin.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLogin ResLogin = new ResLogin();
        User currentUserDB = userService.getUserByEmail(reqLogin.getUsername());
        if (currentUserDB != null) {
            ResLogin.UserLogin userLogin = new ResLogin.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getFullname(),
                    currentUserDB.getPhone(),
                    currentUserDB.getAddress(),
                    currentUserDB.getRole());
            ResLogin.setUser(userLogin);
        }

        String access_token = this.securityUtil.createAccessToken(reqLogin.getUsername(), ResLogin.getUser());
        String refresh_token = this.securityUtil.createRefreshToken(reqLogin.getUsername(), ResLogin);
        userService.updateUserToken(refresh_token, reqLogin.getUsername());
        ResLogin.setAccess_token(access_token);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(ResLogin);
    }

    @GetMapping("/account")
    @ApiMessage("Get profile")
    public ResponseEntity<ResLogin.UserGetAccount> getProfile() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUserDB = userService.getUserByEmail(email);
        ResLogin.UserLogin userLogin = new ResLogin.UserLogin();
        ResLogin.UserGetAccount userGetAccount = new ResLogin.UserGetAccount();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setFullname(currentUserDB.getFullname());
            userLogin.setPhone(currentUserDB.getPhone());
            userLogin.setAddress(currentUserDB.getAddress());
            userLogin.setRoleName(currentUserDB.getRole());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }

   

    @GetMapping("/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLogin> getAllgetRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "empty") String refresh_token) throws InvalidException {
        if (refresh_token.equals("empty")) {
            throw new InvalidException("Bạn không có refresh token ở cookie");
        }
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();

        // check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new InvalidException("Refresh Token không hợp lệ");
        }
        // issue new token/set refresh token as cookies
        ResLogin res = new ResLogin();
        User currentUserDB = userService.getUserByEmail(email);
        if (currentUserDB != null) {
            ResLogin.UserLogin userLogin = new ResLogin.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    currentUserDB.getFullname(),
                    currentUserDB.getAddress(),
                    currentUserDB.getPhone(),
                    currentUserDB.getRole());
            res.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(email, res.getUser());
        res.setAccess_token(access_token);

        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

        userService.updateUserToken(new_refresh_token, email);

        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws InvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        if (email.isEmpty()) {
            throw new InvalidException("Access Token không hợp lệ");
        }

        this.userService.updateUserToken(null, email);

        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", "null")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .build();
    }

    @PostMapping("/register")
    @ApiMessage("Register a new patient")
    public ResponseEntity<ReqRegister> register(@Valid @RequestBody ReqRegister reqRegister) throws InvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.registerUser(reqRegister));
    }
}
