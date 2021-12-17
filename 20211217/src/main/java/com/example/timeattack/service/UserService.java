package com.example.timeattack.service;

import com.example.timeattack.domain.User;
import com.example.timeattack.dto.request.UserRequestDto;
import com.example.timeattack.dto.response.LoginResponseDto;
import com.example.timeattack.repository.UserRepository;
import com.example.timeattack.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public User createUser(UserRequestDto userRequestDto) {
        String userId = userRequestDto.getUserId();
        // 사용자이름 중복 확인
        Optional<User> found = userRepository.findByUserId(userId);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 이름이 존재합니다.");
        }

        String password = passwordEncoder.encode(userRequestDto.getPassword());

        User user = new User(userId, password);
        return userRepository.save(user);
    }

    public ResponseEntity<?> createAuthenticationToken(UserRequestDto userRequestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDto.getUserId(), userRequestDto.getPassword()));

        final String accessToken = jwtTokenUtil.generateAccessToken(userRequestDto.getUserId());
        return ResponseEntity.ok(new LoginResponseDto(userRequestDto.getUserId(), accessToken));
    }
}
