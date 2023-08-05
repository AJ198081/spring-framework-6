package dev.aj.services.implementation;

import dev.aj.domain.model.Book;
import dev.aj.repositories.BookRepository;
import dev.aj.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Set<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

}
