package dev.aj.service;

import dev.aj.domain.model.Author;
import dev.aj.repository.AuthorRepository;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@Transactional(noRollbackForClassName = "Author")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private List<Author> authors = null;


    @BeforeEach
    void setUp() {
        Author aj = Author.builder().name("AJ").build();
        Author dj = Author.builder().name("DJ").build();
        Author pw = Author.builder().name("PW").build();
        Author tl = Author.builder().name("TL").build();
        Author mp = Author.builder().name("MP").build();

        authors = new ArrayList<>();

        authors.addAll(List.of(aj, dj, pw, tl, mp));
    }

    @AfterEach
    void tearDown() {
        authors = null;
    }

    @Test
    void saveAllAuthors() {
        List<Author> savedAuthors = authorRepository.saveAll(authors);
        savedAuthors.stream()
                .forEach(System.out::println);

    }
}