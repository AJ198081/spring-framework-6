package dev.aj.controllers;

import dev.aj.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @RequestMapping(path = "authors")
    public String viewAllAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors-view";
    }

}
