package ru.spbguap.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbguap.bookstore.dto.BookDto;
import ru.spbguap.bookstore.model.Book;
import ru.spbguap.bookstore.model.Publisher;
import ru.spbguap.bookstore.repository.BookRepository;
import ru.spbguap.bookstore.repository.PublisherRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Бизнес-логика для работы с книгами на базе Spring Data JPA и Hibernate.
 * Реализует транзакционные операции чтения/записи, DTO-трансформацию и аудит.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuditService auditService;

    /** Добавить новую книгу (запись в БД через JPA + аудит). */
    public BookDto create(BookDto dto) {
        Book book = toEntity(dto);
        Book saved = bookRepository.save(book);
        auditService.logCreate("Book", saved.getId());
        return toDto(saved);
    }

    /** Получить список всех книг. */
    @Transactional(readOnly = true)
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Найти книгу по ID. */
    @Transactional(readOnly = true)
    public Optional<BookDto> getById(Long id) {
        return bookRepository.findById(id).map(this::toDto);
    }

    /** Обновить данные книги (запись в БД + аудит). */
    public Optional<BookDto> update(Long id, BookDto dto) {
        return bookRepository.findById(id).map(existing -> {
            existing.setTitle(dto.getTitle());
            existing.setAuthor(dto.getAuthor());
            existing.setGenre(dto.getGenre());
            existing.setYear(dto.getYear());
            existing.setPrice(dto.getPrice());
            existing.setStock(dto.getStock());

            if (dto.getPublisherId() != null) {
                Publisher publisher = publisherRepository.findById(dto.getPublisherId()).orElse(null);
                existing.setPublisher(publisher);
            }

            Book updated = bookRepository.save(existing);
            auditService.logUpdate("Book", updated.getId());
            return toDto(updated);
        });
    }

    /** Удалить книгу по ID (запись в БД + аудит). */
    public boolean delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            auditService.logDelete("Book", id, true);
            return true;
        }
        auditService.logDelete("Book", id, false);
        return false;
    }

    public BookDto toDto(Book entity) {
        if (entity == null) return null;
        return BookDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .genre(entity.getGenre())
                .year(entity.getYear())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .publisherId(entity.getPublisher() != null ? entity.getPublisher().getId() : null)
                .publisherName(entity.getPublisher() != null ? entity.getPublisher().getName() : null)
                .build();
    }

    public Book toEntity(BookDto dto) {
        if (dto == null) return null;
        Publisher publisher = null;
        if (dto.getPublisherId() != null) {
            publisher = publisherRepository.findById(dto.getPublisherId()).orElse(null);
        }
        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .genre(dto.getGenre())
                .year(dto.getYear())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .publisher(publisher)
                .build();
    }
}
