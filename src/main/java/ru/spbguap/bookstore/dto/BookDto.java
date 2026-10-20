package ru.spbguap.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Data Transfer Object для сущности «Книга».
 * Позволяет передавать данные в REST API без раскрытия внутренней
 * структуры JPA и без риска циклических ссылок.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO книги для REST API")
public class BookDto {

    @Schema(description = "Идентификатор книги", example = "1")
    private Long id;

    @Schema(description = "Название книги", example = "Война и мир")
    private String title;

    @Schema(description = "Автор книги", example = "Лев Толстой")
    private String author;

    @Schema(description = "Жанр книги", example = "Роман")
    private String genre;

    @Schema(description = "Год издания", example = "1869")
    private Integer year;

    @Schema(description = "Цена книги в рублях", example = "590.00")
    private Double price;

    @Schema(description = "Количество экземпляров на складе", example = "10")
    private Integer stock;

    @Schema(description = "Идентификатор издательства", example = "1")
    private Long publisherId;

    @Schema(description = "Наименование издательства", example = "АСТ")
    private String publisherName;
}
