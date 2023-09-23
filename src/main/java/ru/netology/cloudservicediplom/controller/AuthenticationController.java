package ru.netology.cloudservicediplom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudservicediplom.dto.AuthRequest;
import ru.netology.cloudservicediplom.dto.Token;
import ru.netology.cloudservicediplom.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(new Token(authenticationService.getToken(authRequest)));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        authenticationService.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
