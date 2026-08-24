package az.librarycrudapi.controller;

import az.librarycrudapi.dto.AuthorRequestDto;
import az.librarycrudapi.dto.AuthorResponseDto;
import az.librarycrudapi.service.AuthorService;
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
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Tag(name = "Author Controller", description = "Muellif emeliyyatlarinin idare edilmesi (v1)")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @Operation(summary = "Yeni muellif yaradilmasi")
    public ResponseEntity<AuthorResponseDto> create(@Valid @RequestBody AuthorRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Butun muelliflerin siyahisi (Pagination ile)")
    public ResponseEntity<Page<AuthorResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(authorService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID-ye gore muellifin tapilmasi")
    public ResponseEntity<AuthorResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Muellif melumatlarinin yenilenmesi")
    public ResponseEntity<AuthorResponseDto> update(@PathVariable Long id, @Valid @RequestBody AuthorRequestDto dto) {
        return ResponseEntity.ok(authorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Muellifin silinmesi")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
