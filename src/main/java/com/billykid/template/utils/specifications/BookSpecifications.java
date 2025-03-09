package com.billykid.template.utils.specifications;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.billykid.template.entity.Author;
import com.billykid.template.entity.Book;
import com.billykid.template.entity.BookStatus;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class BookSpecifications {

    // Filter by book's title
    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    // Filter by author's name
    public static Specification<Book> hasAuthor(String authorName) {
        return (root, query, criteriaBuilder) -> {
            if (authorName == null || authorName.isEmpty()) {
                return null;
            }
            Join<Book, Author> authorJoin = root.join("author");
            return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.<String>get("name")), "%" + authorName.toLowerCase() + "%");
        };
    }

    // Filter by genres
    public static Specification<Book> hasGenre(List<String> genres) {
        return (root, query, criteriaBuilder) -> {
            if (genres == null || genres.isEmpty()) {
                return null;
            }

            Predicate[] predicates = genres.stream()
                    .map((genre) -> criteriaBuilder.isMember(genre, root.get("genres")))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.and(predicates);
        };
    }

    // Filter by reservation status
    public static Specification<Book> isReserved(boolean reserved) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, BookStatus> bookStatusJoin = root.join("bookStatus");

            return criteriaBuilder.equal(bookStatusJoin.get("isAvailable"), !reserved);
        };
    }
}
