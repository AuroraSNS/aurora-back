package com.center.aurora.service.Auth;

import com.center.aurora.domain.user.AuthProvider;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.exception.CustomException;
import com.center.aurora.exception.dto.ErrorCode;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.Auth.Dto.AuthDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Transactional
    public Long signUp(AuthDto authDto){
        if (userRepository.existsByEmail(authDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
        return userRepository.save(User.builder()
                .name(authDto.getName())
                .email(authDto.getEmail())
                .image(User.DEFAULT_IMAGE_URL)
                .role(Role.USER)
                .provider(AuthProvider.local)
                .password(passwordEncoder.encode(authDto.getPassword()))
                .build()
        ).getId();
    }

    @Transactional
    public Map signIn(AuthDto authDto){
        Map result = new HashMap();
        String token = null;
        Optional<User> optionalUser = userRepository.findByEmail(authDto.getEmail());
        if(!optionalUser.isEmpty()){
            User user = optionalUser.get();
            if(passwordEncoder.matches(authDto.getPassword(),user.getPassword())){
                token = createTokenByUserEntity(user);
            }else{
                if(optionalUser.get().getProvider()!= AuthProvider.local){
                    throw new CustomException(ErrorCode.BAD_LOGIN_PROVIDER);
                }else{
                    throw new CustomException(ErrorCode.BAD_LOGIN_PASSWORD);
                }
            }
        }else{
            throw new CustomException(ErrorCode.BAD_LOGIN_EMAIL);
        }
        result.put("accessToken", token);
        return result;
    }

    public String createTokenByUserEntity(User user){
        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(1).toInstant()))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }
}
