package ru.spbguap.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spbguap.bookstore.model.Book;
import ru.spbguap.bookstore.repository.BookRepository;

import java.util.List;
import java.util.Optional;

/**
 * Бизнес-логика для работы с книгами.
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /** Добавить новую книгу. */
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    /** Получить список всех книг. */
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    /** Найти книгу по ID. */
    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    /** Обновить данные книги. */
    public Optional<Book> update(Long id, Book book) {
        return bookRepository.update(id, book);
    }

    /** Удалить книгу по ID. */
    public boolean delete(Long id) {
        return bookRepository.deleteById(id);
    }
}
