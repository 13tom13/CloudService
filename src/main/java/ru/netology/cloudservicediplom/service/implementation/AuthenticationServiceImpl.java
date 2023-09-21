package ru.netology.cloudservicediplom.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudservicediplom.dto.AuthRequest;
import ru.netology.cloudservicediplom.security.JWTUtil;
import ru.netology.cloudservicediplom.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtTokenUtil;

    @Override
    public String getToken(AuthRequest authRequest) {
        Authentication authentication;
        String jwt;
        try {
            String login = authRequest.getLogin();
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login, authRequest.getPassword()));
         jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
         System.out.println("User with email: " + login);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email or password is incorrect", e);
        }
        return jwt;
    }

    @Override
    public void logout(String token) {

    }
}
