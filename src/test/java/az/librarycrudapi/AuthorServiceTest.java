package az.librarycrudapi;

import az.librarycrudapi.dto.AuthorRequestDto;
import az.librarycrudapi.dto.AuthorResponseDto;
import az.librarycrudapi.entity.Author;
import az.librarycrudapi.exception.ResourceNotFoundException;
import az.librarycrudapi.mapper.AuthorMapper;
import az.librarycrudapi.repository.AuthorRepository;
import az.librarycrudapi.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorRequestDto requestDto;
    private AuthorResponseDto responseDto;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setFullName("Nizami Gencevi");
        author.setCountry("Azerbaycan");

        requestDto = new AuthorRequestDto();
        requestDto.setFullName("Nizami Gencevi");
        requestDto.setCountry("Azerbaycan");

        responseDto = new AuthorResponseDto();
        responseDto.setId(1L);
        responseDto.setFullName("Nizami Gencevi");
        responseDto.setCountry("Azerbaycan");
    }

    @Test
    void testGetById_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toResponseDto(author)).thenReturn(responseDto);

        AuthorResponseDto result = authorService.getById(1L);

        assertNotNull(result);
        assertEquals("Nizami Gencevi", result.getFullName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.getById(1L));
        verify(authorRepository, times(1)).findById(1L);
    }
}
