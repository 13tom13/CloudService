package ru.netology.cloudservicediplom.security;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloudservicediplom.repository.LogoutRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    public final static String BEARER_HEADER = "Bearer ";

    private final JWTUtil jwtUtil;

    private final LogoutRepository logoutRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_HEADER)) {
            if (logoutRepository.findByToken(authorizationHeader).isPresent()) {
                System.out.println("logout is token");
                throw new RuntimeException("Token is logout");
            }
            jwt = authorizationHeader.substring(BEARER_HEADER.length());
            username = jwtUtil.extractUsername(authorizationHeader);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String commaSeparatedListOfAuthorities = jwtUtil.extractAuthorities(jwt);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(commaSeparatedListOfAuthorities);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }
        chain.doFilter(request, response);
    }

}
