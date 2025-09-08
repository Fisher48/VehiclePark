-- Удаление в правильном порядке (сначала таблицы с внешними ключами)
--DROP TABLE IF EXISTS gps_data;
--DROP TABLE IF EXISTS trip;
--DROP TABLE IF EXISTS enterprise_manager;
--DROP TABLE IF EXISTS vehicle;
--DROP TABLE IF EXISTS driver;
--DROP TABLE IF EXISTS manager;
--DROP TABLE IF EXISTS enterprise;
--DROP TABLE IF EXISTS brand;

CREATE SCHEMA IF NOT EXISTS autopark;

SET SCHEMA autopark;

CREATE TABLE IF NOT EXISTS autopark.brand (
    id SERIAL PRIMARY KEY,
    brand_name VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL,
    fuel_tank INT NOT NULL,
    load_capacity INT NOT NULL,
    number_of_seats INT NOT NULL
);

CREATE TABLE IF NOT EXISTS autopark.enterprise (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    timezone VARCHAR(50) DEFAULT 'UTC'
);

CREATE TABLE IF NOT EXISTS autopark.vehicle (
    id SERIAL PRIMARY KEY,
    number VARCHAR(10) NOT NULL UNIQUE,
    price INT NOT NULL,
    mileage INT,
    year_of_car_production INT NOT NULL,
    brand_id INT,
    enterprise_id INT,
    purchase_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    active_driver_id INT,
    CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE SET NULL,
    CONSTRAINT fk_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprise(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS autopark.trip (
    id SERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    enterprise_id BIGINT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    start_gps_data_id INT,
    end_gps_data_id INT,
    mileage NUMERIC(10, 2) DEFAULT 0 NOT NULL,
    -- Используем BIGINT для хранения длительности в миллисекундах
    -- duration BIGINT GENERATED ALWAYS AS (DATEDIFF('MS', start_time, end_time)) STORED,
    CONSTRAINT fk_trip_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
    CONSTRAINT fk_trip_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprise(id)
);

CREATE TABLE IF NOT EXISTS autopark.driver (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    salary NUMERIC(10, 2) NOT NULL,
    active BOOLEAN DEFAULT FALSE,
    enterprise_id INT,
    active_vehicle_id INT UNIQUE,
    CONSTRAINT fk_driver_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprise(id) ON DELETE SET NULL,
    CONSTRAINT fk_driver_vehicle FOREIGN KEY (active_vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS autopark.driver_vehicle (
    driver_id INT,
    vehicle_id INT,
    PRIMARY KEY (driver_id, vehicle_id),
    CONSTRAINT fk_dv_driver FOREIGN KEY (driver_id) REFERENCES driver(id) ON DELETE CASCADE,
    CONSTRAINT fk_dv_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS autopark.manager (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    chat_id BIGINT
);

CREATE TABLE IF NOT EXISTS autopark.enterprise_manager (
    enterprise_id INT,
    manager_id INT,
    PRIMARY KEY (enterprise_id, manager_id),
    CONSTRAINT fk_em_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprise(id) ON DELETE SET NULL,
    CONSTRAINT fk_em_manager FOREIGN KEY (manager_id) REFERENCES manager(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS autopark.person (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS autopark.app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS autopark.gps_data (
    id SERIAL PRIMARY KEY,
    vehicle_id INT,
    coordinates GEOMETRY, --VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    trip_id BIGINT,
    CONSTRAINT fk_gps_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE SET NULL,
    CONSTRAINT fk_gps_trip FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS autopark.report (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    period VARCHAR(50) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL
);