package az.librarycrudapi.service.impl;

import az.librarycrudapi.dto.AuthRequestDto;
import az.librarycrudapi.dto.AuthResponseDto;
import az.librarycrudapi.entity.User;
import az.librarycrudapi.enums.Role;
import az.librarycrudapi.exception.BadCredentialsException;
import az.librarycrudapi.exception.ResourceNotFoundException;
import az.librarycrudapi.exception.UserAlreadyExistsException;
import az.librarycrudapi.repository.UserRepository;
import az.librarycrudapi.service.AuthService;
import az.librarycrudapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String register(AuthRequestDto dto) {
        log.info("Qeydiyyat sorgusu daxil oldu. Username: {}", dto.getUsername());

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            log.error("Username artiq movcuddur: {}", dto.getUsername());
            throw new UserAlreadyExistsException("Username artiq movcuddur");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        log.info("Istifadeci ugurla qeydiyyatdan kecdi. Username: {}", dto.getUsername());
        return "Istifadeci ugurla qeydiyyatdan kecdi";
    }

    @Override
    public AuthResponseDto login(AuthRequestDto dto) {
        log.info("Giris sorgusu daxil oldu. Username: {}", dto.getUsername());

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Istifadeci tapilmadi"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.error("Sifre yanlisdir: {}", dto.getUsername());
            throw new BadCredentialsException("Sifre yanlisdir");
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        log.info("Giris ugurlu oldu, Access ve Refresh tokenler yaradildi. Username: {}", dto.getUsername());
        return new AuthResponseDto(accessToken, refreshToken);
    }
}
