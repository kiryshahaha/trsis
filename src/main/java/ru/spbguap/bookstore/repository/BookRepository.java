package ru.spbguap.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbguap.bookstore.model.Book;

import java.util.List;

/**
 * Spring Data JPA репозиторий для сущности Book.
 * Предоставляет полный набор CRUD-операций и кастомных запросов через Hibernate.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByPublisherId(Long publisherId);
}
