package com.StackDash.Configuration;

import com.StackDash.Entity.Role;
import com.StackDash.Entity.RoleName;
import com.StackDash.Entity.User;
import com.StackDash.Repository.UserRepository;
import com.StackDash.Service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtills {

    Logger log = LoggerFactory.getLogger(JwtUtills.class);
    @Value("${spring.app.jwtSecret}")
    private String secretKey;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationTime;

    @Autowired
    private UserRepository userService;

    public String getJwtFromHeader(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            log.info("JWT token fetched from header: {}", bearerToken);
            return bearerToken.substring(7).trim();
        }
        log.warn("Bearer token is null or malformed: { }");
        return null;
    }


    public String generateToken(UserDetails userDetail) {
        String username = userDetail.getUsername();

        //Fetch userId and roles from your database
        Optional<User> userOtp = userService.findByUserName(username);
        if (userOtp.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userOtp.get();
        Long userId = user.getUserId();

        //  Extract role names
        Set<RoleName> roles = user.getRoles().stream()
                .map(Role::getName) // assuming Role has getName()
                .collect(Collectors.toSet());

        //  Build token with claims
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles) // Add roles to token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(Key(), Jwts.SIG.HS256)
                .compact();
    }


    private SecretKey Key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


    public String getUsernameFromJwt(String token){
        log.info("getting username from jwt: {}", token);
        return Jwts.parser().verifyWith((SecretKey) Key()).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean validateJwtToken(String token){
        try {
           // log.info("Token validated success: {}", claims);
            Jwts.parser().verifyWith((SecretKey) Key()).build().parseSignedClaims(token);
            //log.info("Token validated success: {}", claims);
            System.out.println("Token Validated");
            log.info("Token validated success: { }");
            return true;
        }catch (MalformedJwtException e) {
            System.out.println("Invalid jwt token " + e.getMessage());
            log.error("Invalid jwt token" + e.getMessage());
        }catch (ExpiredJwtException e) {
            log.error("Jwt token is expired : " + e.getMessage());
        }catch (UnsupportedJwtException e) {
            log.error("jwt token is unsupported : " + e.getMessage());
        }catch (IllegalArgumentException e) {
            log.error("Jwt token string is empty : " + e.getMessage());
        }
        return false;
    }
}
