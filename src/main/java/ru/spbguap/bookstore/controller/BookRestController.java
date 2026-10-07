package ru.spbguap.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spbguap.bookstore.model.Book;
import ru.spbguap.bookstore.service.BookService;

import java.util.List;

/**
 * REST-контроллер для работы с книгами книжного магазина.
 * Реализует полный набор CRUD-операций.
 *
 * Базовый путь: /api/books
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Книги", description = "API для управления каталогом книг книжного магазина")
public class BookRestController {

    private final BookService bookService;

    // ──────────────────────────────────────────────────────────────────
    // POST /api/books — Создать новую книгу
    // ──────────────────────────────────────────────────────────────────
    @Operation(
            summary = "Добавить книгу",
            description = "Создаёт новую запись о книге в каталоге магазина. " +
                          "Возвращает созданный объект с присвоенным ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Книга успешно создана",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book created = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ──────────────────────────────────────────────────────────────────
    // GET /api/books — Получить список всех книг
    // ──────────────────────────────────────────────────────────────────
    @Operation(
            summary = "Получить все книги",
            description = "Возвращает полный список книг из каталога магазина."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список книг успешно получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

    // ──────────────────────────────────────────────────────────────────
    // GET /api/books/{id} — Получить книгу по ID
    // ──────────────────────────────────────────────────────────────────
    @Operation(
            summary = "Получить книгу по ID",
            description = "Возвращает данные конкретной книги по её уникальному идентификатору."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Книга найдена",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Книга с указанным ID не найдена",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID книги", example = "1")
            @PathVariable Long id) {
        return bookService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ──────────────────────────────────────────────────────────────────
    // PUT /api/books/{id} — Обновить книгу полностью
    // ──────────────────────────────────────────────────────────────────
    @Operation(
            summary = "Обновить книгу",
            description = "Полностью заменяет данные книги с указанным ID. " +
                          "Все поля должны быть переданы в теле запроса."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Книга успешно обновлена",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Книга с указанным ID не найдена",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID книги", example = "1")
            @PathVariable Long id,
            @RequestBody Book book) {
        return bookService.update(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ──────────────────────────────────────────────────────────────────
    // DELETE /api/books/{id} — Удалить книгу
    // ──────────────────────────────────────────────────────────────────
    @Operation(
            summary = "Удалить книгу",
            description = "Удаляет книгу с указанным ID из каталога магазина."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Книга успешно удалена",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Книга с указанным ID не найдена",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID книги", example = "1")
            @PathVariable Long id) {
        if (bookService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
