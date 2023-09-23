package ru.netology.cloudservicediplom.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloudservicediplom.model.CloudUser;
import ru.netology.cloudservicediplom.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JWTUtil jwtUtil;

    private final static String DEFAULT_ROLE = "USER";

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        CloudUser user = userRepository.findByLogin(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + userName);
        }
        return User.builder()
                .username(user.getLogin())
                .password(jwtUtil.bcryptEncoder().encode(user.getPassword()))
                .roles(DEFAULT_ROLE)
                .build();
    }
}
