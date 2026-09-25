package az.librarycrudapi.controller;

import az.librarycrudapi.dto.BookRequestDto;
import az.librarycrudapi.dto.BookResponseDto;
import az.librarycrudapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "Kitab emeliyyatlarinin idare edilmesi (v1)")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Yeni kitabin yaradilmasi")
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto dto) {
        BookResponseDto created = bookService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Butun kitablarin siyahisi (Pagination ile)")
    public ResponseEntity<Page<BookResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID-ye gore kitabin tapilmasi")
    public ResponseEntity<BookResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Kitab melumatlarinin yenilenmesi")
    public ResponseEntity<BookResponseDto> update(@PathVariable Long id, @Valid @RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kitabin silinmesi")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Kitablarin dinamik axtarilmasi")
    public ResponseEntity<Page<BookResponseDto>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minDiscount,
            @RequestParam(required = false) Double maxDiscount,
            Pageable pageable) {
        return ResponseEntity.ok(bookService.searchBooks(title, author, minPrice, maxPrice, minDiscount, maxDiscount, pageable));
    }
}
