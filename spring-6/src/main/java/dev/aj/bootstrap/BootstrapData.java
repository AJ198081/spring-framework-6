package dev.aj.bootstrap;

import dev.aj.domain.model.Author;
import dev.aj.domain.model.Book;
import dev.aj.domain.model.Publisher;
import dev.aj.repositories.AuthorRepository;
import dev.aj.repositories.BookRepository;
import dev.aj.repositories.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    public static final String DDD_ISBN = "123456789";
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    @Override
    public void run(String... args) throws Exception {

        Publisher myPublisher = Publisher.builder()
                .publisherName("My Publisher")
                .address("123 Black Street")
                .build();

        Publisher savedMyPublisher = publisherRepository.save(myPublisher);

        Author eric = Author.builder()
                .firstName("Eric")
                .lastName("Evans")
                .build();

        Book ddd = Book.builder()
                .title("Domain Driven Design")
                .isbn(DDD_ISBN)
                .publisher(myPublisher)
                .build();

        Author rod = Author.builder()
                .firstName("Rod")
                .lastName("Johnson")
                .build();

        Book noEJB = Book.builder()
                .title("J2EE Development without EJB")
                .isbn("123456788")
                .publisher(myPublisher)
                .build();

        Author ericSaved = authorRepository.save(eric);

        Author rodSaved = authorRepository.save(rod);

        ddd.addAuthor(ericSaved);

        noEJB.addAuthor(rodSaved);

        bookRepository.save(noEJB);

        bookRepository.save(ddd);
    }
}
