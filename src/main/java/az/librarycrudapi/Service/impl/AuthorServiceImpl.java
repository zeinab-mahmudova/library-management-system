package az.librarycrudapi.service.impl;

import az.librarycrudapi.dto.AuthorRequestDto;
import az.librarycrudapi.dto.AuthorResponseDto;
import az.librarycrudapi.entity.Author;
import az.librarycrudapi.exception.ResourceNotFoundException;
import az.librarycrudapi.mapper.AuthorMapper;
import az.librarycrudapi.repository.AuthorRepository;
import az.librarycrudapi.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorResponseDto create(AuthorRequestDto dto) {
        log.info("Yeni muellif yaradilir: {}", dto.getFullName());
        Author author = authorMapper.toEntity(dto);
        Author saved = authorRepository.save(author);
        log.info("Muellif ugurla yaradildi. ID: {}", saved.getId());
        return authorMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorResponseDto> getAll(Pageable pageable) {
        log.info("Butun muelliflerin siyahisi teleb olunur");
        return authorRepository.findAll(pageable).map(authorMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponseDto getById(Long id) {
        log.info("Muellif axtarilir. ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Muellif tapilmadi! ID: {}", id);
                    return new ResourceNotFoundException("Muellif tapilmadi. ID: " + id);
                });
        return authorMapper.toResponseDto(author);
    }

    @Override
    @Transactional
    public AuthorResponseDto update(Long id, AuthorRequestDto dto) {
        log.info("Muellif melumatlari yenilenir. ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Yenilenme ugursuz oldu. Muellif tapilmadi! ID: {}", id);
                    return new ResourceNotFoundException("Muellif tapilmadi. ID: " + id);
                });

        authorMapper.updateEntityFromDto(dto, author);
        Author updated = authorRepository.save(author);
        log.info("Muellif melumatlari ugurla yenilendi. ID: {}", id);
        return authorMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.warn("Muellif silinir. ID: {}", id);
        if (!authorRepository.existsById(id)) {
            log.error("Silinme ugursuz oldu. Muellif tapilmadi! ID: {}", id);
            throw new ResourceNotFoundException("Muellif tapilmadi. ID: " + id);
        }
        authorRepository.deleteById(id);
        log.info("Muellif ugurla silindi. ID: {}", id);
    }
}
