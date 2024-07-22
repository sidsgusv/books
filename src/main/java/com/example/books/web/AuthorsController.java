package com.example.books.web;

import com.example.books.model.Author;
import com.example.books.model.Book;
import com.example.books.repository.AuthorRepository;
import com.example.books.repository.AuthorsSearchSpecification;
import com.example.books.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthorsController implements AuthorsNamespace{
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorsController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    @GetMapping
    public List<Author> getAll(@RequestParam (required = false) String authorName,
                               @RequestParam (required = false) String bookTitle){
        return authorRepository.findAll(new AuthorsSearchSpecification(authorName,bookTitle));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id){
        Optional<Author> authorOpt = authorRepository.findById(id);
        return authorOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/{authorId}/books")
    public List<Book> findBooks (@PathVariable Long authorId){
        Author author = authorRepository.findById(authorId)
                .get();

        return bookRepository.findBookByAuthor(author);


    }

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author,
                                         UriComponentsBuilder ucBuilder){

        Author newAuthor=authorRepository.save(author);
        return ResponseEntity.created(
                ucBuilder.path("/authors/{authorId}").buildAndExpand(newAuthor.getId()).toUri()
        ).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Author> deleteById(@PathVariable Long id) {
        authorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
