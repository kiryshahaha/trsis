package ru.spbguap.bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Data Transfer Object для сущности «Издательство».
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO издательства")
public class PublisherDto {

    @Schema(description = "Идентификатор издательства", example = "1")
    private Long id;

    @Schema(description = "Наименование издательства", example = "АСТ")
    private String name;

    @Schema(description = "Город издательства", example = "Москва")
    private String city;

    @Schema(description = "Контактный телефон", example = "+7 (495) 123-45-67")
    private String contactPhone;
}
