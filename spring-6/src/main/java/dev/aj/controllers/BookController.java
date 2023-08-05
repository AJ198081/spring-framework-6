package dev.aj.controllers;

import dev.aj.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping(path = "/books")
    public String getBooks(Model modelAndView) {
        modelAndView.addAttribute("books", bookService.getAllBooks());
        return "books-view";
    }


}
