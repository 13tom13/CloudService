package ru.netology.cloudservicediplom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
