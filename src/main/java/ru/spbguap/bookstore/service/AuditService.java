package ru.spbguap.bookstore.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Сервис аудита — Лабораторная работа №4, задание 4:
 * «Защитите ресурсы, требующие запись и введите аудит».
 *
 * Каждое изменяющее действие (CREATE / UPDATE / DELETE) фиксируется
 * в системном логе и сохраняется в памяти для вывода на фронтенде.
 */
@Service
public class AuditService {

    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<AuditRecord> records = Collections.synchronizedList(new ArrayList<>());

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditRecord {
        private String timestamp;
        private String user;
        private String action;
        private String entity;
        private String details;
    }

    /** Получить логин текущего аутентифицированного пользователя. */
    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    private void addRecord(String action, String entity, String details) {
        String user = currentUser();
        String time = LocalDateTime.now().format(FORMATTER);
        records.add(0, new AuditRecord(time, user, action, entity, details));
    }

    /** Аудит создания объекта. */
    public void logCreate(String entity, Object id) {
        auditLog.info("[AUDIT] user='{}' action=CREATE entity={} id={}", currentUser(), entity, id);
        addRecord("CREATE", entity, "Создан объект с ID=" + id);
    }

    /** Аудит обновления объекта. */
    public void logUpdate(String entity, Object id) {
        auditLog.info("[AUDIT] user='{}' action=UPDATE entity={} id={}", currentUser(), entity, id);
        addRecord("UPDATE", entity, "Обновлен объект с ID=" + id);
    }

    /** Аудит удаления объекта. */
    public void logDelete(String entity, Object id, boolean success) {
        auditLog.info("[AUDIT] user='{}' action=DELETE entity={} id={} success={}",
                currentUser(), entity, id, success);
        addRecord("DELETE", entity, "Удаление объекта с ID=" + id + " (успех=" + success + ")");
    }

    /** Аудит попытки несанкционированного доступа. */
    public void logAccessDenied(String resource) {
        auditLog.warn("[AUDIT] user='{}' action=ACCESS_DENIED resource={}", currentUser(), resource);
        addRecord("ACCESS_DENIED", "Resource", "Запрещен доступ к " + resource);
    }

    /** Получить все записи аудита (для отображения на фронтенде). */
    public List<AuditRecord> getAllRecords() {
        return new ArrayList<>(records);
    }
}
