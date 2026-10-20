package ru.spbguap.bookstore.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность «Книга» — основная JPA-модель книжного магазина.
 * Связана отношением @ManyToOne с сущностью Publisher (внешний ключ publisher_id).
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "JPA-модель книги")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор книги", example = "1")
    private Long id;

    @Column(nullable = false, length = 200)
    @Schema(description = "Название книги", example = "Война и мир")
    private String title;

    @Column(nullable = false, length = 150)
    @Schema(description = "Автор книги", example = "Лев Толстой")
    private String author;

    @Column(length = 100)
    @Schema(description = "Жанр книги", example = "Роман")
    private String genre;

    @Column(name = "pub_year")
    @Schema(description = "Год издания", example = "1869")
    private Integer year;

    @Column(nullable = false)
    @Schema(description = "Цена книги в рублях", example = "590.00")
    private Double price;

    @Column(nullable = false)
    @Schema(description = "Количество экземпляров на складе", example = "10")
    private Integer stock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    @Schema(description = "Издательство, выпустившее книгу")
    private Publisher publisher;
}
