package az.librarycrudapi.repository;

import az.librarycrudapi.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Book> hasAuthor(String authorName) {
        return (root, query, criteriaBuilder) -> {
            if (authorName == null || authorName.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("fullName")), "%" + authorName.toLowerCase() + "%");
        };
    }

    public static Specification<Book> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.conjunction();
            }
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<Book> discountBetween(Double minDiscount, Double maxDiscount) {
        return (root, query, criteriaBuilder) -> {
            if (minDiscount == null && maxDiscount == null) {
                return criteriaBuilder.conjunction();
            }
            if (minDiscount != null && maxDiscount != null) {
                return criteriaBuilder.between(root.get("discount"), minDiscount, maxDiscount);
            }
            if (minDiscount != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), minDiscount);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("discount"), maxDiscount);
        };
    }
}
