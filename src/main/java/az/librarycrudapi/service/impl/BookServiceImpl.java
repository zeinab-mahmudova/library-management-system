package az.librarycrudapi.service.impl;

import az.librarycrudapi.dto.BookRequestDto;
import az.librarycrudapi.dto.BookResponseDto;
import az.librarycrudapi.entity.Author;
import az.librarycrudapi.entity.Book;
import az.librarycrudapi.exception.ResourceNotFoundException;
import az.librarycrudapi.mapper.BookMapper;
import az.librarycrudapi.repository.AuthorRepository;
import az.librarycrudapi.repository.BookRepository;
import az.librarycrudapi.repository.BookSpecification;
import az.librarycrudapi.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponseDto create(BookRequestDto dto) {
        log.info("Yeni kitab yaradilir: Basliq = {}", dto.getTitle());
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author tapilmadi " + dto.getAuthorId()));
        Book book = bookMapper.toEntity(dto);
        book.setAuthor(author);
        Book saved = bookRepository.save(book);
        log.info("Kitab ugurla yaradildi. ID: {}", saved.getId());
        return bookMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> getAll(Pageable pageable) {
        log.info("Butun kitablarin siyahisi teleb olunur");
        return bookRepository.findAll(pageable).map(bookMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#id")
    public BookResponseDto getById(Long id) {
        log.info("Kitab axtarilir. ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitab tapilmadi " + id));
        return bookMapper.toResponseDto(book);
    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public BookResponseDto update(Long id, BookRequestDto dto) {
        log.info("Kitab melumatlari yenilenir. ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitab tapilmadi " + id));
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author tapilmadi " + dto.getAuthorId()));
        bookMapper.updateEntityFromDto(dto, book);
        book.setAuthor(author);
        Book updated = bookRepository.save(book);
        log.info("Kitab ugurla yenilendi. ID: {}", id);
        return bookMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public void delete(Long id) {
        log.warn("Kitab silinir. ID: {}", id);
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Kitab tapilmadi " + id);
        }
        bookRepository.deleteById(id);
        log.info("Kitab ugurla silindi. ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> searchBooks(String title, String author, Double minPrice, Double maxPrice, Double minDiscount, Double maxDiscount, Pageable pageable) {
        log.info("Dinamik kitab axtarisi ise salindi");
        Specification<Book> spec = BookSpecification.hasTitle(title)
                .and(BookSpecification.hasAuthor(author))
                .and(BookSpecification.priceBetween(minPrice, maxPrice))
                .and(BookSpecification.discountBetween(minDiscount, maxDiscount));
        return bookRepository.findAll(spec, pageable).map(bookMapper::toResponseDto);
    }
}
