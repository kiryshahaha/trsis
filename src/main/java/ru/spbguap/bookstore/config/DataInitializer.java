package ru.spbguap.bookstore.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.spbguap.bookstore.model.Book;
import ru.spbguap.bookstore.model.Publisher;
import ru.spbguap.bookstore.repository.BookRepository;
import ru.spbguap.bookstore.repository.PublisherRepository;

/**
 * Инициализатор начальных данных для демонстрации работы Hibernate и связей JPA.
 * Создает издательства и книги, связанные внешним ключом.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        if (publisherRepository.count() == 0) {
            log.info("Инициализация начальных данных БД: издательства и книги (JPA/Hibernate)...");

            Publisher ast = publisherRepository.save(Publisher.builder()
                    .name("АСТ")
                    .city("Москва")
                    .contactPhone("+7 (495) 789-45-00")
                    .build());

            Publisher eksmo = publisherRepository.save(Publisher.builder()
                    .name("Эксмо")
                    .city("Москва")
                    .contactPhone("+7 (495) 411-68-86")
                    .build());

            Publisher azbuka = publisherRepository.save(Publisher.builder()
                    .name("Азбука-Аттикус")
                    .city("Санкт-Петербург")
                    .contactPhone("+7 (812) 327-04-55")
                    .build());

            bookRepository.save(Book.builder()
                    .title("Война и мир")
                    .author("Лев Толстой")
                    .genre("Роман")
                    .year(1869)
                    .price(590.0)
                    .stock(5)
                    .publisher(ast)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Преступление и наказание")
                    .author("Фёдор Достоевский")
                    .genre("Роман")
                    .year(1866)
                    .price(450.0)
                    .stock(8)
                    .publisher(eksmo)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Мастер и Маргарита")
                    .author("Михаил Булгаков")
                    .genre("Фантастика")
                    .year(1967)
                    .price(520.0)
                    .stock(12)
                    .publisher(azbuka)
                    .build());

            log.info("Инициализация данных JPA завершена: загружено {} издательств и {} книг.",
                    publisherRepository.count(), bookRepository.count());
        }
    }
}
