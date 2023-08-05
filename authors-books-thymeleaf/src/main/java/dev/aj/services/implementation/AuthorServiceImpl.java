package dev.aj.services.implementation;

import dev.aj.domain.model.Author;
import dev.aj.repositories.AuthorRepository;
import dev.aj.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Set<Author> getAllAuthors() {
        return authorRepository.findAllAuthors();
    }
}
