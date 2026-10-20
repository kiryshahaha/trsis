package ru.spbguap.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spbguap.bookstore.model.Publisher;

import java.util.Optional;

/**
 * Spring Data JPA репозиторий для сущности Publisher.
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
}
