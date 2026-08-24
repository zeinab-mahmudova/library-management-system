package az.librarycrudapi.service;

import az.librarycrudapi.dto.AuthRequestDto;
import az.librarycrudapi.dto.AuthResponseDto;

public interface AuthService {
    String register(AuthRequestDto dto);
    AuthResponseDto login(AuthRequestDto dto);
}
