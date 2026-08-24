package az.librarycrudapi.service;

import az.librarycrudapi.dto.MemberRequestDto;
import az.librarycrudapi.dto.MemberResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberResponseDto create(MemberRequestDto dto);
    Page<MemberResponseDto> getAll(Pageable pageable);
    MemberResponseDto getById(Long id);
    MemberResponseDto update(Long id, MemberRequestDto dto);
    void delete(Long id);
}
