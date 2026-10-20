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
import ru.spbguap.bookstore.dto.PublisherDto;
import ru.spbguap.bookstore.service.PublisherService;

import java.util.List;

/**
 * REST-контроллер для управления издательствами (вторая таблица JPA).
 * Базовый путь: /api/publishers
 */
@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
@Tag(name = "Издательства", description = "API для управления издательствами книг (вторая сущность JPA)")
public class PublisherRestController {

    private final PublisherService publisherService;

    @Operation(summary = "Получить все издательства", description = "Возвращает перечень всех издательств из БД.")
    @ApiResponse(responseCode = "200", description = "Список издательств успешно получен",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PublisherDto.class))))
    @GetMapping
    public ResponseEntity<List<PublisherDto>> getAll() {
        return ResponseEntity.ok(publisherService.getAll());
    }

    @Operation(summary = "Получить издательство по ID", description = "Возвращает данные издательства по ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Издательство найдено",
                    content = @Content(schema = @Schema(implementation = PublisherDto.class))),
            @ApiResponse(responseCode = "404", description = "Издательство не найдено", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDto> getById(
            @Parameter(description = "ID издательства", example = "1")
            @PathVariable Long id) {
        return publisherService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Добавить издательство", description = "Создает новое издательство в базе данных.")
    @ApiResponse(responseCode = "201", description = "Издательство успешно создано",
            content = @Content(schema = @Schema(implementation = PublisherDto.class)))
    @PostMapping
    public ResponseEntity<PublisherDto> create(@RequestBody PublisherDto dto) {
        PublisherDto created = publisherService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Обновить издательство", description = "Обновляет реквизиты издательства.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Издательство обновлено",
                    content = @Content(schema = @Schema(implementation = PublisherDto.class))),
            @ApiResponse(responseCode = "404", description = "Издательство не найдено", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDto> update(
            @Parameter(description = "ID издательства", example = "1")
            @PathVariable Long id,
            @RequestBody PublisherDto dto) {
        return publisherService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить издательство", description = "Удаляет запись издательства из БД.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Издательство удалено", content = @Content),
            @ApiResponse(responseCode = "404", description = "Издательство не найдено", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID издательства", example = "1")
            @PathVariable Long id) {
        if (publisherService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
