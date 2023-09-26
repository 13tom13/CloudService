package ru.netology.cloudservicediplom.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.netology.cloudservicediplom.dto.AuthRequest;
import ru.netology.cloudservicediplom.exception.CloudServiceUnauthorizedError;
import ru.netology.cloudservicediplom.model.LogoutToken;
import ru.netology.cloudservicediplom.repository.LogoutRepository;
import ru.netology.cloudservicediplom.security.JWTUtil;
import ru.netology.cloudservicediplom.service.AuthenticationService;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtTokenUtil;

    private final LogoutRepository logoutRepository;

    @Override
    public String getToken(AuthRequest authRequest) {
        Authentication authentication;
        String jwt;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
         jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        } catch (BadCredentialsException e) {
            throw new CloudServiceUnauthorizedError("Email or password is incorrect");
        }
        return jwt;
    }

    @Override
    public void logout(String token) {
        logoutRepository.save(new LogoutToken(token));
        log.info(format("User with username [%s] is logout", jwtTokenUtil.extractUsername(token)));
    }
}
