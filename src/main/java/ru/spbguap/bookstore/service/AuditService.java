package ru.spbguap.bookstore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Сервис аудита — Лабораторная работа №4, задание 4:
 * «Защитите ресурсы, требующие запись и введите аудит».
 *
 * Каждое изменяющее действие (CREATE / UPDATE / DELETE) фиксируется
 * в логе вместе с именем пользователя, выполнившего операцию.
 */
@Service
public class AuditService {

    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");

    /** Получить логин текущего аутентифицированного пользователя. */
    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    /** Аудит создания объекта. */
    public void logCreate(String entity, Object id) {
        auditLog.info("[AUDIT] user='{}' action=CREATE entity={} id={}", currentUser(), entity, id);
    }

    /** Аудит обновления объекта. */
    public void logUpdate(String entity, Object id) {
        auditLog.info("[AUDIT] user='{}' action=UPDATE entity={} id={}", currentUser(), entity, id);
    }

    /** Аудит удаления объекта. */
    public void logDelete(String entity, Object id, boolean success) {
        auditLog.info("[AUDIT] user='{}' action=DELETE entity={} id={} success={}",
                currentUser(), entity, id, success);
    }

    /** Аудит попытки несанкционированного доступа. */
    public void logAccessDenied(String resource) {
        auditLog.warn("[AUDIT] user='{}' action=ACCESS_DENIED resource={}", currentUser(), resource);
    }
}
