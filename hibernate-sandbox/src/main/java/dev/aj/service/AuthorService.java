package dev.aj.service;

import dev.aj.domain.model.Author;
import dev.aj.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> saveAllAuthors(List<Author> authors) {
        return authorRepository.saveAll(authors);
    }

}
