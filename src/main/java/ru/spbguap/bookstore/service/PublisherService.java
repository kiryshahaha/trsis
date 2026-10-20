package ru.spbguap.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbguap.bookstore.dto.PublisherDto;
import ru.spbguap.bookstore.model.Publisher;
import ru.spbguap.bookstore.repository.PublisherRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис управления издательствами с поддержкой транзакций Spring/Hibernate.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public List<PublisherDto> getAll() {
        return publisherRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PublisherDto> getById(Long id) {
        return publisherRepository.findById(id).map(this::toDto);
    }

    public PublisherDto create(PublisherDto dto) {
        Publisher publisher = Publisher.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .contactPhone(dto.getContactPhone())
                .build();
        Publisher saved = publisherRepository.save(publisher);
        auditService.logCreate("Publisher", saved.getId());
        return toDto(saved);
    }

    public Optional<PublisherDto> update(Long id, PublisherDto dto) {
        return publisherRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setCity(dto.getCity());
            existing.setContactPhone(dto.getContactPhone());
            Publisher updated = publisherRepository.save(existing);
            auditService.logUpdate("Publisher", updated.getId());
            return toDto(updated);
        });
    }

    public boolean delete(Long id) {
        if (publisherRepository.existsById(id)) {
            publisherRepository.deleteById(id);
            auditService.logDelete("Publisher", id, true);
            return true;
        }
        auditService.logDelete("Publisher", id, false);
        return false;
    }

    public PublisherDto toDto(Publisher entity) {
        if (entity == null) return null;
        return PublisherDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .contactPhone(entity.getContactPhone())
                .build();
    }
}
