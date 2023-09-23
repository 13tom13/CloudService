package ru.netology.cloudservicediplom.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.netology.cloudservicediplom.security.JWTFilter.BEARER_HEADER;


@Service
@RequiredArgsConstructor
public class JWTUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.sessionTime}")
    private long sessionTime;

    @Bean
    public PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 12);
    }

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }


    // генерация токена (кладем в него имя пользователя и authorities)
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String commaSeparatedListOfAuthorities=
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        claims.put("authorities", commaSeparatedListOfAuthorities);
        return createToken(claims, userDetails.getUsername());
    }

    //извлечение имени пользователя из токена (внутри валидация токена)
    public String extractUsername(String token) {
        return extractClaim(token.substring(BEARER_HEADER.length()), Claims::getSubject);
    }

    //извлечение authorities (внутри валидация токена)
    public String extractAuthorities(String token) {
        Function<Claims, String> claimsListFunction = claims -> (String)claims.get("authorities");
        return extractClaim(token, claimsListFunction);
    }



    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expireTimeFromNow())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }


    private Date expireTimeFromNow() {
        return new Date(System.currentTimeMillis() + sessionTime);
    }
}