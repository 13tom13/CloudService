package ru.netology.cloudservicediplom.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudservicediplom.dto.AuthRequest;
import ru.netology.cloudservicediplom.dto.Token;
import ru.netology.cloudservicediplom.service.AuthenticationService;

import static java.lang.String.format;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody AuthRequest authRequest) {
        Token authToken = new Token(authenticationService.getToken(authRequest));
        log.info(format("User with username [%s] is login", authRequest.getLogin()));
        return ResponseEntity.ok(authToken);
    }

    @GetMapping("/login")
    public ResponseEntity<Token> login(@RequestHeader("auth-token") String authToken) {
        authenticationService.logout(authToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        authenticationService.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
