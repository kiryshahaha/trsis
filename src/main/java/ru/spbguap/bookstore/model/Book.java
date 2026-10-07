package ru.spbguap.bookstore.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность «Книга» — основная модель книжного магазина.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель книги")
public class Book {

    @Schema(description = "Уникальный идентификатор книги", example = "1")
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
}
