package ru.netology.cloudservicediplom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservicediplom.security.AuthRequest;
import ru.netology.cloudservicediplom.security.JWTUtil;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
            System.out.println(authentication);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Имя или пароль неправильны", e);
        }
        // при создании токена в него кладется username как Subject и список authorities как кастомный claim
        String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok()
                .header("auth-token", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authRequest);
    }

    @PostMapping("/user")
    public ResponseEntity<?> user(@RequestHeader("auth-token") String jwt, @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok()
                .body(String.format("Hi, %s! \ntoken: %s", authRequest.getLogin(), jwt));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String jwt) {
       return ResponseEntity.ok()
               .body("Success logout");
    }
}
