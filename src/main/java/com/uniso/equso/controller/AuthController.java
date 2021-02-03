package com.uniso.equso.controller;

import com.uniso.equso.config.security.SecurityConstant;
import com.uniso.equso.exceptions.AuthenticationException;
import com.uniso.equso.model.users.CreateUserRequest;
import com.uniso.equso.model.auth.JwtResponse;
import com.uniso.equso.model.auth.LoginRequest;
import com.uniso.equso.service.AuthService;
import com.uniso.equso.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("${url.root}")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    public static final String SIGN_UP = "sign-up";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";

    private final AuthService authService;
    private final UserService userService;


    @PostMapping(LOGIN)
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.createAuthenticationToken(request));
    }

    @PostMapping(SIGN_UP)
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("ActionLog.createUser.started");
        userService.addUser(request);
        log.info("ActionLog.createUser.ended");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(LOGOUT)
    public void logout(HttpServletRequest request) {
        log.info("ActionLog.logout.started");
        var token = Optional.of(request.getHeader(SecurityConstant.HEADER_STRING))
                .orElseThrow(() -> {
                    throw new AuthenticationException("exception.authentication.header-not-found");
                });
        authService.logout(token);
        log.info("ActionLog.logout.ended");
    }
}
