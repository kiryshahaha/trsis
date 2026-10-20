package ru.spbguap.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Веб-контроллер страниц для Лабораторных работ №3 и №4.
 *
 * Обрабатывает:
 *  - /login -> страница входа (form-based authentication, методичка стр. 48-49)
 *  - /      -> перенаправление на /books
 *  - /books -> главная страница каталога с AngularJS (методичка стр. 33-36, 50)
 */
@Controller
public class WebPageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/books")
    public String booksPage() {
        return "books";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/books";
    }
}
