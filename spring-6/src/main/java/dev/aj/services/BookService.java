package dev.aj.services;

import dev.aj.domain.model.Book;

import java.util.Set;

public interface BookService {
    Set<Book> getAllBooks();
}
