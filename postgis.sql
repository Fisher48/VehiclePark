-- Создание схемы

CREATE SCHEMA IF NOT EXISTS autopark;


-- Установка поиска в схеме autopark

SET search_path TO autopark;


-- Создание расширения PostGIS в схеме autopark

CREATE EXTENSION IF NOT EXISTS postgis SCHEMA autopark;


-- Создание таблицы с использованием типа geometry

CREATE TABLE gps_data (
    id integer NOT NULL,
    vehicle_id integer,
    coordinates geometry(Point,4326),
    "timestamp" timestamp with time zone DEFAULT now() NOT NULL,
    trip_id bigint
);
