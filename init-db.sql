-- ==============================================================================
-- Скрипт первичной инициализации базы данных PostgreSQL в Docker
-- Автоматически исполняется PostgreSQL из /docker-entrypoint-initdb.d/
-- ==============================================================================

-- Таблица 1: Издательства (Publishers)
CREATE TABLE IF NOT EXISTS publishers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100),
    contact_phone VARCHAR(50)
);

-- Таблица 2: Книги (Books) со связью по внешнему ключу publisher_id
CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(150) NOT NULL,
    genre VARCHAR(100),
    pub_year INT,
    price DOUBLE PRECISION NOT NULL,
    stock INT NOT NULL,
    publisher_id BIGINT REFERENCES publishers(id) ON DELETE SET NULL
);

-- Первичное наполнение тестовыми данными
INSERT INTO publishers (name, city, contact_phone) VALUES
    ('АСТ', 'Москва', '+7 (495) 789-45-00'),
    ('Эксмо', 'Москва', '+7 (495) 411-68-86'),
    ('Азбука-Аттикус', 'Санкт-Петербург', '+7 (812) 327-04-55')
ON CONFLICT DO NOTHING;

INSERT INTO books (title, author, genre, pub_year, price, stock, publisher_id) VALUES
    ('Война и мир', 'Лев Толстой', 'Роман', 1869, 590.0, 5, 1),
    ('Преступление и наказание', 'Фёдор Достоевский', 'Роман', 1866, 450.0, 8, 2),
    ('Мастер и Маргарита', 'Михаил Булгаков', 'Фантастика', 1967, 520.0, 12, 3)
ON CONFLICT DO NOTHING;
