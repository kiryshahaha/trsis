package ru.spbguap.bookstore.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность «Издательство» (Publisher).
 * Вторая таблица для выполнения требований Лабораторной работы №6
 * (связь один-ко-многим с книгами).
 */
@Entity
@Table(name = "publishers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность издательства книги")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор издательства", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Наименование издательства", example = "АСТ")
    private String name;

    @Column(length = 100)
    @Schema(description = "Город расположения издательства", example = "Москва")
    private String city;

    @Column(length = 50)
    @Schema(description = "Контактный телефон", example = "+7 (495) 123-45-67")
    private String contactPhone;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Book> books = new ArrayList<>();
}
