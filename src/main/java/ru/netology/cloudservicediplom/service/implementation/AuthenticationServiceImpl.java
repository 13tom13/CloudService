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
import ru.netology.cloudservicediplom.model.LogoutToken;
import ru.netology.cloudservicediplom.repository.LogoutRepository;
import ru.netology.cloudservicediplom.security.JWTUtil;
import ru.netology.cloudservicediplom.service.AuthenticationService;

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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email or password is incorrect", e);
        }
        return jwt;
    }

    @Override
    public void logout(String token) {
        System.out.println("token is logout");
        logoutRepository.save(new LogoutToken(token));
    }
}
