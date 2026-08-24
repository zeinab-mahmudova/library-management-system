package az.librarycrudapi.mapper;

import az.librarycrudapi.dto.BookRequestDto;
import az.librarycrudapi.dto.BookResponseDto;
import az.librarycrudapi.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "borrowedBy", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Book toEntity(BookRequestDto dto);

    @Mapping(target = "authorName", source = "author.fullName")
    @Mapping(target = "borrowedByMemberId", source = "borrowedBy.id")
    BookResponseDto toResponseDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "borrowedBy", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    void updateEntityFromDto(BookRequestDto dto, @MappingTarget Book book);
}
