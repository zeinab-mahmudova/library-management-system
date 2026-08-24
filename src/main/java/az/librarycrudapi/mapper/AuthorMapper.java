package az.librarycrudapi.mapper;

import az.librarycrudapi.dto.AuthorRequestDto;
import az.librarycrudapi.dto.AuthorResponseDto;
import az.librarycrudapi.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorRequestDto dto);

    AuthorResponseDto toResponseDto(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntityFromDto(AuthorRequestDto dto, @MappingTarget Author author);
}
