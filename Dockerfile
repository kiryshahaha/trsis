# ==============================================================================
# Dockerfile для микросервиса «Книжный магазин» (Лабораторная работа №8)
# Базовый образ: легковесная среда исполнения Eclipse Temurin OpenJDK 17 (JRE)
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="K. A. Engelgardt <spbguap@example.com>"
LABEL version="1.0.0"
LABEL description="Bookstore Microservice — TRSIS Lab 8 (SPb GUAP)"

# Рабочая директория внутри контейнера
WORKDIR /app

# Копирование предварительно собранного Spring Boot JAR-артефакта
COPY target/bookstore-1.0.0.jar app.jar

# Публикация защищенного порта HTTPS (8443)
EXPOSE 8443

# Настройка переменных виртуальной машины Java
ENV JAVA_OPTS="-Xms256m -Xmx512m -Dfile.encoding=UTF-8"

# Точка входа для запуска микросервиса
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
