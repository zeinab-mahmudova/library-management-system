package az.librarycrudapi.service;

import az.librarycrudapi.dto.AuthorRequestDto;
import az.librarycrudapi.dto.AuthorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponseDto create(AuthorRequestDto dto);
    Page<AuthorResponseDto> getAll(Pageable pageable);
    AuthorResponseDto getById(Long id);
    AuthorResponseDto update(Long id, AuthorRequestDto dto);
    void delete(Long id);
}
