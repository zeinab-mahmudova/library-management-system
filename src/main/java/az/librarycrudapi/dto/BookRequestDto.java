package az.librarycrudapi.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestDto {

    @NotBlank(message = "Kitab adi bos ola bilmez")
    private String title;

    @NotBlank(message = "ISBN bos ola bilmez")
    private String isbn;

    @Min(value = 1000, message = "Il xetali daxil edilib")
    private Integer publicationYear;

    @NotNull(message = "Qiymet daxil edilmelidir")
    @PositiveOrZero(message = "Qiymet menfi ola bilmez")
    private Double price;

    @NotNull(message = "Endirim daxil edilmelidir")
    @PositiveOrZero(message = "Endirim menfi ola bilmez")
    private Double discount;

    @NotNull(message = "Muellif ID daxil edilmelidir")
    private Long authorId;
}
