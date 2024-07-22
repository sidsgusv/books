package com.example.books.repository;

import com.example.books.model.Author;
import com.example.books.model.Book;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class AuthorsSearchSpecification implements Specification <Author> {


    private final String authorName;
    private final String bookTitle;

    public AuthorsSearchSpecification(String authorName, String bookTitle) {


        this.authorName = authorName;
        this.bookTitle = bookTitle;
    }



    @Override
    public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate p=criteriaBuilder.conjunction();


        if(authorName!=null) {
            p.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.equal(root.get("name"), authorName))
                    );

        }
        if (bookTitle!=null) {
            Subquery<Book>bookSubquery=query.subquery(Book.class);
            Root<Book> subqueryRoot=bookSubquery.from(Book.class);
            bookSubquery.select(subqueryRoot);

            bookSubquery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root,subqueryRoot.get("author")),
                            criteriaBuilder.equal(subqueryRoot.get("title"), bookTitle)
                    )
            );
            p.getExpressions().add(criteriaBuilder.exists(bookSubquery));
        }

        return p;
    }
}
