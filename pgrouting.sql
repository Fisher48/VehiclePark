-- Создание схемы (если нужно)
CREATE SCHEMA IF NOT EXISTS autopark;

-- Установка поиска в схеме autopark
SET search_path TO autopark;

CREATE EXTENSION IF NOT EXISTS pgrouting SCHEMA autopark;
