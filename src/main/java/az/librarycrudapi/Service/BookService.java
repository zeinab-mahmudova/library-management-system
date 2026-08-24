package az.librarycrudapi.service;

import az.librarycrudapi.dto.BookRequestDto;
import az.librarycrudapi.dto.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto create(BookRequestDto dto);
    Page<BookResponseDto> getAll(Pageable pageable);
    BookResponseDto getById(Long id);
    BookResponseDto update(Long id, BookRequestDto dto);
    void delete(Long id);
    Page<BookResponseDto> searchBooks(String title, String author, Double minPrice, Double maxPrice, Double minDiscount, Double maxDiscount, Pageable pageable);
}
