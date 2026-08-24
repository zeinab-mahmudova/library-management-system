package az.librarycrudapi.service.impl;

import az.librarycrudapi.dto.MemberRequestDto;
import az.librarycrudapi.dto.MemberResponseDto;
import az.librarycrudapi.entity.Member;
import az.librarycrudapi.exception.ResourceNotFoundException;
import az.librarycrudapi.mapper.MemberMapper;
import az.librarycrudapi.repository.MemberRepository;
import az.librarycrudapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public MemberResponseDto create(MemberRequestDto dto) {
        log.info("Yeni uzv yaradilir: {}", dto.getFullName());
        Member member = memberMapper.toEntity(dto);
        Member saved = memberRepository.save(member);
        log.info("Uzv ugurla yaradildi. ID: {}", saved.getId());
        return memberMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberResponseDto> getAll(Pageable pageable) {
        log.info("Butun uzvlerin siyahisi teleb olunur");
        return memberRepository.findAll(pageable).map(memberMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getById(Long id) {
        log.info("Uzv axtarilir. ID: {}", id);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uzv tapilmadi " + id));
        return memberMapper.toResponseDto(member);
    }

    @Override
    @Transactional
    public MemberResponseDto update(Long id, MemberRequestDto dto) {
        log.info("Uzv melumatlari yenilenir. ID: {}", id);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uzv tapilmadi " + id));

        memberMapper.updateEntityFromDto(dto, member);
        Member updated = memberRepository.save(member);
        log.info("Uzv melumatlari ugurla yenilendi. ID: {}", id);
        return memberMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.warn("Uzv silinir. ID: {}", id);
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Uzv tapilmadi " + id);
        }
        memberRepository.deleteById(id);
        log.info("Uzv ugurla silindi. ID: {}", id);
    }
}
