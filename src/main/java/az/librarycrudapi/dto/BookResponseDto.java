package az.librarycrudapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Double price;
    private Double discount;
    private String authorName;
    private Long borrowedByMemberId;
}
