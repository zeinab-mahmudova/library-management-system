package az.librarycrudapi.mapper;

import az.librarycrudapi.dto.MemberRequestDto;
import az.librarycrudapi.dto.MemberResponseDto;
import az.librarycrudapi.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Member toEntity(MemberRequestDto dto);

    MemberResponseDto toResponseDto(Member member);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateEntityFromDto(MemberRequestDto dto, @MappingTarget Member member);
}
