package ru.spbguap.bookstore.repository;

import org.springframework.stereotype.Repository;
import ru.spbguap.bookstore.model.Book;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Простое in-memory хранилище книг (без БД, достаточно для лаб. работы).
 * Данные хранятся в ConcurrentHashMap, что обеспечивает потокобезопасность.
 */
@Repository
public class BookRepository {

    private final Map<Long, Book> store = new LinkedHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    /** Инициализируем несколько тестовых записей при старте */
    public BookRepository() {
        save(Book.builder()
                .title("Война и мир")
                .author("Лев Толстой")
                .genre("Роман")
                .year(1869)
                .price(590.0)
                .stock(5)
                .build());

        save(Book.builder()
                .title("Преступление и наказание")
                .author("Фёдор Достоевский")
                .genre("Роман")
                .year(1866)
                .price(450.0)
                .stock(8)
                .build());

        save(Book.builder()
                .title("Мастер и Маргарита")
                .author("Михаил Булгаков")
                .genre("Фантастика")
                .year(1967)
                .price(520.0)
                .stock(12)
                .build());
    }

    /** Сохранить новую книгу, назначив ей ID. */
    public Book save(Book book) {
        book.setId(idSequence.getAndIncrement());
        store.put(book.getId(), book);
        return book;
    }

    /** Получить все книги. */
    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    /** Найти книгу по ID. */
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /** Обновить существующую книгу. Возвращает обновлённый объект или empty, если не найдена. */
    public Optional<Book> update(Long id, Book updated) {
        if (!store.containsKey(id)) {
            return Optional.empty();
        }
        updated.setId(id);
        store.put(id, updated);
        return Optional.of(updated);
    }

    /** Удалить книгу по ID. Возвращает true если удалена, false если не найдена. */
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }
}
