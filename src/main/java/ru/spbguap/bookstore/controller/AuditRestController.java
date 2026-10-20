package ru.spbguap.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spbguap.bookstore.service.AuditService;

import java.util.List;

/**
 * REST-контроллер для доступа к журналу аудита безопасности (только для роли ADMIN).
 */
@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Tag(name = "Аудит", description = "Журнал аудита действий пользователей (ЛР №4)")
public class AuditRestController {

    private final AuditService auditService;

    @Operation(summary = "Получить записи аудита безопасности", description = "Возвращает список зафиксированных действий пользователей.")
    @GetMapping
    public ResponseEntity<List<AuditService.AuditRecord>> getAuditLogs() {
        return ResponseEntity.ok(auditService.getAllRecords());
    }
}
