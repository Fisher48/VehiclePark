-- Вставка данных в таблицу brand
INSERT INTO autopark.brand (brand_name, type, fuel_tank, load_capacity, number_of_seats)
VALUES ('Toyota', 'Sedan', 50, 500, 5),
       ('Volvo', 'Truck', 150, 2000, 2),
       ('Honda', 'SUV', 60, 600, 7);

-- Вставка данных в таблицу enterprise
INSERT INTO autopark.enterprise (name, city, timezone)
VALUES ('ABC Logistics', 'New York', 'Europe/Moscow'),
       ('IHP Company', 'Lipetsk', 'America/New_York'),
       ('Test LTD', 'Moscow', 'Asia/Tokyo');

-- Вставка данных в таблицу vehicle
INSERT INTO autopark.vehicle (number, year_of_car_production, price, mileage, purchase_time, brand_id, enterprise_id)
VALUES
('К831СУ02', 1976, 631963, 3799, '2005-02-20 17:08:00', 3, 1),
('К320ВО23', 2010, 730117, 203751, '2014-07-24 23:37:00', 1, 1),
('К877АВ62', 1993, 374891, 305380, '2017-05-18 14:10:00', 1, 1),
('Х383МВ78', 1984, 780950, 305602, '2013-05-15 09:06:00', 1, 1),
('О918ОЕ96', 1950, 618780, 85252, '2016-08-10 15:51:00', 3, 1),
('Н89ЕЕ08', 2013, 377474, 291147, '2011-03-13 14:43:00', 2, 1),
('О570ТО117', 1954, 391100, 357880, '2021-01-29 03:28:00', 2, 1),
('Н621ОА55', 1995, 777531, 252967, '2023-01-14 01:26:00', 2, 1),
('Е771МУ112', 2019, 218512, 380602, '2014-03-16 02:20:00', 2, 1),
('Е886СК111', 1998, 167308, 245074, '2008-04-01 12:46:00', 1, 1),
('В939УЕ22', 1969, 264464, 372529, '2023-01-13 09:38:00', 1, 1),
('Р256ТС88', 1982, 782752, 226755, '2012-06-20 02:10:00', 3, 1),
('А729ЕР172', 1951, 161601, 365927, '2016-07-21 21:01:00', 1, 1),
('К598УА112', 2010, 712523, 276807, '2019-03-02 09:45:00', 1, 1),
('Т394НС160', 1992, 241154, 136359, '2016-04-15 10:24:00', 3, 1),
('У650ТЕ48', 1968, 355258, 278118, '2014-11-07 23:18:00', 3, 1),
('Т593АК93', 2000, 288877, 89797, '2018-08-30 22:53:00', 2, 1),
('Х177СЕ160', 1996, 843145, 133630, '2016-05-15 07:21:00', 1, 1),
('Х264ЕС128', 1991, 941622, 12725, '2022-04-24 20:44:00', 2, 1),
('К219ВА26', 1974, 428199, 366763, '2004-06-22 06:56:00', 3, 1),
('К390МО157', 1996, 354089, 377710, '2020-03-07 14:31:00', 2, 1),
('К281ВН163', 1982, 367281, 223618, '2007-02-18 23:43:00', 1, 1),
('С650УС101', 1969, 879114, 26204, '2017-12-15 03:18:00', 2, 1),
('Х310НВ147', 1998, 259082, 45962, '2007-05-31 07:35:00', 3, 1),
('С015ЕВ65', 1970, 367809, 215785, '2013-11-22 04:30:00', 3, 1);
--(26, 'У263ХХ107', 2001, 407685, 157135, '2011-09-05 19:47:00', 1, 2),
--(27, 'В864НО12', 1970, 555256, 220609, '2005-04-15 7:24:00', 1, 2),
--(28, 'Х415РН65', 1952, 699010, 168718, '2010-11-10 16:18:00', 1, 2),
--(29, 'Х427РА99', 1959, 720860, 17338, '2021-12-27 19:09:00', 3, 2),
--(30, 'В531АУ168', 1977, 833738, 13234, '2024-04-20 15:53:00', 1, 2),
--(31, 'Е808УУ137', 1975, 124482, 125699, '2014-07-22 13:12:00', 2, 2),
--(32, 'С340ОХ122', 1960, 842826, 391518, '2015-11-05 6:58:00', 1, 2),
--(33, 'М287ВВ180', 1979, 450667, 356718, '2023-03-08 13:37:00', 1, 2),
--(34, 'У553МТ78', 1986, 853986, 61409, '2005-11-26 5:56:00', 1, 2),
--(35, 'А974АР48', 1965, 153897, 390524, '2013-12-11 20:25:00', 2, 2),
--(36, 'Н311ТМ117', 1988, 595604, 384325, '2011-12-28 3:51:00', 3, 2),
--(37, 'О454ОМ70', 1966, 739736, 117241, '2020-05-30 3:06:00', 3, 2),
--(38, 'О413ЕТ136', 1988, 205714, 196116, '2010-08-22 18:05:00', 3, 2),
--(39, 'Е362ВМ47', 1984, 600370, 53458, '2005-03-17 12:28:00', 3, 2),
--(40, 'А607УК143', 2021, 864957, 80161, '2023-02-08 3:12:00', 1, 2),
--(41, 'Н166КУ33', 2014, 681399, 73651, '2018-04-16 23:16:00', 1, 2),
--(42, 'А370АЕ172', 1989, 518472, 351486, '2011-03-10 14:08:00', 2, 2),
--(43, 'О640ВТ36', 1997, 626847, 182384, '2011-12-22 21:17:00', 1, 2),
--(44, 'Х932АУ48', 1974, 179223, 360602, '2019-11-10 6:44:00', 1, 2),
--(45, 'В587МХ16', 2023, 218081, 163901, '2010-01-04 21:51:00', 3, 2),
--(46, 'В25СУ174', 2001, 588969, 395636, '2014-06-22 23:57:00', 2, 2),
--(47, 'М474ТВ50', 1974, 680120, 355254, '2016-08-20 11:51:00', 3, 2),
--(48, 'Р860МО123', 1989, 240366, 274317, '2019-07-19 2:13:00', 2, 2),
--(49, 'М552КК108', 1974, 588290, 355160, '2016-02-21 21:35:00', 2, 2),
--(50, 'У806ОР149', 2019, 839766, 146972, '2015-08-16 4:43:00', 1, 2),
--(51, 'Е807КМ01', 1971, 720971, 337339, '2012-09-25 19:44:00', 3, 3),
--(52, 'Н792ВВ129', 1987, 665214, 330286, '2014-06-14 16:33:00', 1, 3),
--(53, 'Е798СВ104', 1976, 109988, 64163, '2012-07-25 13:13:00', 3, 3),
--(54, 'К151СО35', 1957, 349486, 362534, '2019-03-23 20:56:00', 1, 3),
--(55, 'У269ТА160', 1955, 131600, 210609, '2013-01-25 6:45:00', 3, 3),
--(56, 'Н862АК153', 2020, 149658, 55851, '2007-09-18 16:15:00', 1, 3),
--(57, 'В25АН154', 2016, 950305, 29329, '2010-11-29 13:14:00', 2, 3),
--(58, 'С603НТ138', 1957, 586806, 294582, '2006-03-05 2:26:00', 3, 3),
--(59, 'Р79ТК98', 1988, 631735, 179988, '2008-06-27 14:06:00', 3, 3),
--(60, 'Р748ВК103', 2022, 603365, 26838, '2010-08-22 4:14:00', 3, 3),
--(61, 'В895АР160', 1987, 387452, 236084, '2013-07-28 8:51:00', 2, 3),
--(62, 'О603ТЕ160', 1959, 331924, 100094, '2019-09-23 14:05:00', 2, 3),
--(63, 'А863КТ112', 1975, 458609, 84343, '2009-02-07 2:05:00', 1, 3),
--(64, 'О713МА87', 2024, 485527, 144713, '2006-12-16 11:09:00', 3, 3),
--(65, 'Р847АО114', 1973, 876038, 158855, '2015-04-17 23:51:00', 3, 3),
--(66, 'Х847ХВ98', 2010, 924888, 157249, '2007-06-03 20:46:00', 3, 3),
--(67, 'О382УН175', 1955, 937787, 22745, '2018-05-16 19:53:00', 1, 3),
--(68, 'К689ОХ140', 1995, 212344, 6802, '2017-11-17 9:12:00', 2, 3),
--(69, 'Т991РХ03', 1987, 921111, 13830, '2005-07-31 14:19:00', 2, 3),
--(70, 'У550ОЕ34', 1951, 669709, 219072, '2024-03-20 9:58:00', 1, 3),
--(71, 'В979РХ158', 1983, 511594, 232619, '2011-01-09 7:36:00', 1, 3),
--(72, 'Е177УС15', 2018, 837079, 45412, '2007-06-26 1:50:00', 3, 3),
--(73, 'У936ВУ61', 1988, 643550, 367294, '2020-01-18 21:26:00', 1, 3),
--(74, 'С494ЕВ129', 1990, 564339, 361859, '2019-07-09 8:19:00', 1, 3),
--(75, 'М205КТ71', 1992, 660234, 137658, '2008-01-20 23:08:00', 2, 3);

-- Вставка данных в таблицу driver
INSERT INTO autopark.driver (name, salary, enterprise_id, active, active_vehicle_id) VALUES
('Сидоров Д. Д.', 445367, 1, FALSE, NULL),
('Иванов М. И.', 165331, 1, FALSE, NULL),
('Смирнов А. П.', 221495, 1, FALSE, NULL),
('Петров А. Д.', 447853, 2, FALSE, NULL),
('Сидоров П. М.', 63023, 2, FALSE, NULL),
('Иванов А. М.', 170221, 2, FALSE, NULL),
('Смирнов М. Д.', 369801, 3, FALSE, NULL),
('Иванов М. И.', 54025, 3, FALSE, NULL),
('Иванов А. А.', 55607, 3, FALSE, NULL),
('Кузнецов Д. Д.', 235519, 1, TRUE, 1),
('Смирнов А. А.', 290520, 1, TRUE, 5),
('Петров М. М.', 224654, 2, TRUE, 12),
('Петров П. И.', 233990, 2, TRUE, 16),
('Смирнов М. Д.', 121675, 3, TRUE, 21),
('Кузнецов А. П.', 296251, 3, TRUE, 24);

-- Вставка данных в таблицу manager
INSERT INTO autopark.manager (username, password, role)
VALUES ('Ivan', '$2a$12$cwz8mAdKkpT3iOQLn7yfBOjRcaptB9oJBp9weYjLn7afOKzMzI2sa', 'MANAGER'),
       ('Pups', '$2a$12$ipGDghqQiblvhfZu1NLqNuBVKgamQuQgDdMVnBA5uKs7ofXuJFJ0W', 'MANAGER');

-- Вставка данных в таблицу enterprise_manager
INSERT INTO autopark.enterprise_manager (enterprise_id, manager_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 2);

-- Вставка данных в таблицу gps_data
INSERT INTO autopark.gps_data (vehicle_id, coordinates, timestamp)
VALUES (1, 'POINT(37.6176 55.7558)', '2021-01-06 16:27:34'),  -- Красная площадь
       (1, 'POINT(37.6189 55.7560)', '2022-12-14 16:28:00'),
       (1, 'POINT(37.6250 55.7580)', '2022-12-14 16:29:00'),
       (1, 'POINT(37.6350 55.7620)', '2022-12-14 16:30:00'),
       (1, 'POINT(37.6500 55.7680)', '2022-12-14 16:31:00'),  -- Выезд на МКАД
       (1, 'POINT(37.7000 55.7700)', '2022-12-14 16:32:00'),  -- МКАД север
       (1, 'POINT(37.7500 55.7750)', '2021-01-06 16:33:24'),  -- МКАД северо-восток
       (2, 'POINT(37.4149 55.9728)', '2023-12-14 09:00:00'),  -- Шереметьево
       (2, 'POINT(37.4300 55.9000)', '2023-12-14 09:15:00'),
       (2, 'POINT(37.5000 55.8500)', '2023-12-14 09:30:00'),
       (2, 'POINT(37.6000 55.8000)', '2023-12-14 09:45:00'),
       (2, 'POINT(37.7000 55.7500)', '2023-12-14 10:00:00'),
       (2, 'POINT(37.7500 55.7000)', '2023-12-14 10:15:00'),
       (2, 'POINT(37.8000 55.6500)', '2023-12-14 10:30:00'),  -- Домодедово
       (2, 'POINT(37.9063 55.4143)', '2023-12-14 10:45:00'),  -- Точка в Домодедово
       (3, 'POINT(37.6176 55.7558)', '2025-05-14 11:00:00'),  -- Центр
       (3, 'POINT(37.6000 55.8000)', '2025-05-14 11:15:00'),
       (3, 'POINT(37.5500 55.8500)', '2025-05-14 11:30:00'),
       (3, 'POINT(37.5000 55.9000)', '2025-05-14 11:45:00'),
       (3, 'POINT(37.4500 55.9500)', '2025-05-14 12:00:00'),
       (3, 'POINT(37.4000 56.0000)', '2025-05-14 12:15:00'),  -- Зеленоград
       (3, 'POINT(37.2562 56.0097)', '2025-05-14 12:30:00'),  -- Центр Зеленограда
       (4, 'POINT(37.7000 55.7700)', '2024-12-14 13:00:00'),  -- Север МКАД
       (4, 'POINT(37.8000 55.7000)', '2024-12-14 13:20:00'),  -- Восток МКАД
       (4, 'POINT(37.7000 55.6000)', '2024-12-14 13:40:00'),  -- Юг МКАД
       (4, 'POINT(37.5000 55.6500)', '2024-12-14 14:00:00'),  -- Запад МКАД
       (4, 'POINT(37.4000 55.7200)', '2024-12-14 14:20:00'),  -- Северо-запад МКАД
       (4, 'POINT(37.6000 55.7500)', '2024-12-14 14:40:00'),  -- Возврат
       (5, 'POINT(37.6176 55.7558)', '2024-12-14 15:00:00'),  -- Красная площадь
       (5, 'POINT(37.6400 55.7300)', '2024-12-14 15:10:00'),  -- Павелецкая
       (5, 'POINT(37.6700 55.7100)', '2024-12-14 15:20:00'),  -- Тульская
       (5, 'POINT(37.6200 55.6800)', '2024-12-14 15:30:00'),  -- Нагатино
       (5, 'POINT(37.5800 55.6600)', '2024-12-14 15:40:00'),  -- Коломенское
       (5, 'POINT(37.5500 55.6300)', '2024-12-14 15:50:00'),  -- Братеево
       (5, 'POINT(37.5000 55.6000)', '2024-12-14 16:00:00');  -- Марьино


--
INSERT INTO autopark.trip (vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage)
VALUES (1, '2021-01-06 16:27:34', '2021-01-06 16:33:24', 1, 7, 52.22),
       (2, '2023-12-14 09:00:00', '2023-12-14 10:45:00', 8, 15, 99.76);
INSERT INTO autopark.trip (vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage)
VALUES (3, '2025-05-14 11:00:00', '2025-05-14 12:30:00', 16, 22, 40.34);
INSERT INTO autopark.trip (vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage)
VALUES (4, '2024-12-14 13:00:00', '2024-12-14 14:40:00', 23, 28, 53.07);
INSERT INTO autopark.trip (vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage)
VALUES (5, '2024-12-14 15:00:00', '2024-12-14 16:00:00', 29, 35, 74.78);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (5, 8758, '2023-12-03 02:33:15', '2023-12-03 04:34:05', 3021, 5177, 81.56);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (6, 1521, '2023-12-01 23:34:29', '2023-12-02 01:55:49', 3022, 5436, 76.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (7, 1521, '2021-04-19 04:12:49', '2021-04-19 06:41:39', 3023, 5486, 97.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (8, 3579, '2022-07-29 21:35:26', '2022-07-29 22:56:06', 5487, 5970, 61.29);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (9, 3579, '2023-03-04 20:05:25', '2023-03-04 21:43:15', 5971, 6557, 81.13);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (10, 4236, '2024-02-24 04:47:39', '2024-02-24 06:57:19', 6558, 7335, 55.08);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (11, 4236, '2024-10-08 16:30:06', '2024-10-08 18:27:26', 7336, 8039, 52.16);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (12, 3151, '2023-08-24 06:25:56', '2023-08-24 08:12:46', 8040, 8680, 57.72);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (13, 3151, '2023-05-11 08:04:39', '2023-05-11 10:16:19', 8681, 9470, 56.21);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (14, 11753, '2023-06-14 02:19:06', '2023-06-14 03:14:46', 9471, 9804, 45.34);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (15, 9317, '2021-05-04 03:00:45', '2021-05-04 05:28:05', 9805, 10688, 61.63);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (16, 2529, '2024-03-28 17:51:47', '2024-03-28 21:18:57', 10689, 11931, 93.09);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (17, 2529, '2023-03-15 20:23:27', '2023-03-15 22:20:07', 11932, 12631, 80.75);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (18, 2042, '2024-11-14 19:55:19', '2024-11-14 21:53:19', 12632, 13750, 69.16);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (19, 9143, '2022-06-05 23:42:56', '2022-06-06 01:35:06', 12927, 14012, 76.78);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (20, 1079, '2024-02-24 15:49:32', '2024-02-24 17:45:12', 14013, 14706, 40.72);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (21, 2042, '2021-08-25 09:30:33', '2021-08-25 10:55:53', 14707, 16622, 64.67);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (22, 9143, '2023-02-16 06:01:35', '2023-02-16 07:52:55', 14742, 17166, 68.07);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (23, 5065, '2023-01-17 02:11:03', '2023-01-17 04:12:03', 14858, 17384, 35.63);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (24, 9143, '2022-07-19 08:54:38', '2022-07-19 11:41:48', 14743, 17615, 74.03);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (25, 1079, '2021-08-07 10:41:39', '2021-08-07 11:57:09', 17616, 18068, 60.84);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (26, 9143, '2024-08-19 12:09:35', '2024-08-19 13:44:35', 18069, 19937, 48.19);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (27, 2042, '2024-01-22 19:37:11', '2024-01-22 21:26:51', 18115, 20312, 54.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (28, 5065, '2021-08-26 15:46:25', '2021-08-26 17:24:25', 18296, 20354, 74.71);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (29, 2042, '2021-07-31 02:14:42', '2021-07-31 05:01:22', 18631, 20884, 41.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (30, 5065, '2024-05-03 02:22:37', '2024-05-03 04:26:37', 20885, 21628, 54.30);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (31, 7184, '2022-11-09 02:38:02', '2022-11-09 03:55:42', 21630, 23478, 48.50);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (32, 13145, '2021-07-06 11:51:03', '2021-07-06 13:28:33', 21629, 23857, 59.71);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (33, 13145, '2024-07-05 12:03:56', '2024-07-05 13:50:56', 21632, 23963, 53.39);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (34, 7184, '2023-03-08 02:43:44', '2023-03-08 05:14:04', 21631, 24223, 67.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (35, 1716, '2023-08-30 00:22:22', '2023-08-30 01:50:02', 24225, 25273, 38.62);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (36, 7184, '2024-07-27 02:15:03', '2024-07-27 04:41:13', 24224, 25626, 84.14);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (37, 13145, '2023-08-18 18:38:02', '2023-08-18 19:45:52', 25627, 26353, 42.47);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (38, 1716, '2022-01-09 04:24:50', '2022-01-09 06:18:40', 25710, 26716, 62.11);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (39, 1716, '2021-10-30 23:17:28', '2021-10-31 01:17:48', 26717, 27438, 64.58);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (40, 13865, '2024-04-25 04:19:14', '2024-04-25 05:52:34', 27439, 27998, 35.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (41, 13865, '2023-05-31 18:01:34', '2023-05-31 20:30:04', 27999, 28889, 71.36);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (42, 13022, '2024-08-21 14:05:42', '2024-08-21 16:53:22', 28890, 29895, 57.83);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (43, 5135, '2023-09-06 13:25:06', '2023-09-06 15:40:06', 29896, 30705, 80.96);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (44, 5135, '2023-01-18 01:21:49', '2023-01-18 02:19:59', 30706, 31054, 25.04);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (45, 5135, '2022-10-31 14:51:22', '2022-10-31 15:51:02', 31055, 31412, 48.08);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (46, 8766, '2022-08-17 16:00:52', '2022-08-17 17:47:42', 31413, 32053, 56.99);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (47, 10854, '2022-07-16 21:25:03', '2022-07-16 22:36:53', 32076, 33546, 29.15);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (48, 10854, '2023-11-11 20:04:01', '2023-11-11 21:25:01', 32054, 33681, 40.07);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (49, 2001, '2024-02-04 06:32:33', '2024-02-04 08:45:53', 32199, 34467, 88.77);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (50, 10854, '2023-01-27 02:41:50', '2023-01-27 04:52:00', 32529, 34551, 63.70);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (51, 2001, '2022-01-30 07:10:16', '2022-01-30 08:57:56', 34796, 36079, 47.58);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (52, 1653, '2023-11-06 19:05:11', '2023-11-06 21:52:11', 34552, 36199, 91.29);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (53, 1653, '2021-10-20 15:21:29', '2021-10-20 17:13:19', 36200, 37501, 70.61);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (54, 2001, '2024-05-01 23:40:50', '2024-05-02 02:27:00', 36225, 37867, 94.76);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (55, 1653, '2021-01-26 08:21:39', '2021-01-26 11:39:19', 37868, 39053, 85.91);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (56, 11401, '2022-02-23 08:50:42', '2022-02-23 10:25:52', 39054, 39624, 66.92);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (57, 11401, '2023-06-10 11:58:15', '2023-06-10 15:23:35', 39625, 40856, 81.45);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (58, 7991, '2023-08-28 11:26:45', '2023-08-28 12:53:35', 40857, 41377, 54.87);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (59, 7991, '2022-06-16 03:35:56', '2022-06-16 05:50:46', 41378, 42186, 63.58);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (60, 8936, '2022-07-22 08:44:31', '2022-07-22 11:34:11', 42187, 43204, 65.29);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (61, 8936, '2023-03-23 03:31:26', '2023-03-23 06:01:16', 43205, 44103, 82.82);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (62, 13079, '2024-01-22 19:56:22', '2024-01-22 21:53:22', 44104, 44805, 74.57);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (63, 13079, '2021-12-25 09:11:19', '2021-12-25 11:22:59', 44806, 45595, 66.41);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (64, 6564, '2024-12-16 10:33:29', '2024-12-16 12:22:29', 45596, 46249, 71.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (65, 6564, '2024-02-27 01:10:51', '2024-02-27 02:48:21', 46250, 46834, 39.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (66, 7228, '2023-12-25 05:44:55', '2023-12-25 07:49:05', 46835, 48129, 65.71);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (67, 7228, '2022-01-04 02:35:09', '2022-01-04 04:26:49', 47025, 48249, 84.12);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (68, 2122, '2021-02-02 01:18:53', '2021-02-02 02:24:23', 48251, 50106, 26.23);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (69, 9624, '2023-07-06 15:31:42', '2023-07-06 16:52:42', 48250, 50472, 32.26);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (70, 2122, '2023-08-21 20:56:28', '2023-08-21 22:17:18', 48371, 50659, 41.21);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (71, 11285, '2022-12-06 17:37:18', '2022-12-06 19:39:58', 48327, 51074, 56.88);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (72, 14525, '2021-12-12 22:17:43', '2021-12-13 00:28:43', 48326, 51135, 59.79);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (73, 7228, '2023-06-26 10:38:50', '2023-06-26 11:34:00', 51159, 51967, 30.27);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (74, 9624, '2023-12-10 02:21:18', '2023-12-10 04:06:38', 51597, 53120, 72.83);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (75, 11285, '2024-08-24 03:59:27', '2024-08-24 07:13:57', 51136, 53265, 97.57);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (76, 7228, '2021-12-06 13:57:03', '2021-12-06 16:25:23', 53266, 54570, 68.98);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (77, 2122, '2024-09-14 11:24:08', '2024-09-14 13:24:48', 53678, 54879, 88.67);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (78, 14525, '2023-11-22 02:12:35', '2023-11-22 04:02:15', 54881, 56230, 44.76);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (79, 11285, '2021-05-31 04:08:55', '2021-05-31 06:55:45', 54880, 57727, 85.61);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (80, 2122, '2021-10-29 18:55:54', '2021-10-29 20:22:14', 56129, 58091, 37.20);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (89, 2122, '2024-04-08 08:55:18', '2024-04-08 11:49:18', 61969, 63523, 88.99);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (90, 14525, '2022-11-05 10:35:02', '2022-11-05 12:43:52', 63524, 64296, 62.30);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (81, 9624, '2022-09-19 22:41:55', '2022-09-20 00:27:55', 56130, 58308, 60.23);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (88, 7228, '2024-11-25 21:40:46', '2024-11-25 23:12:26', 61930, 63218, 28.64);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (82, 9624, '2022-03-28 06:52:24', '2022-03-28 09:09:24', 56131, 58514, 42.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (83, 9624, '2023-12-22 07:16:20', '2023-12-22 08:26:30', 58515, 59289, 31.32);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (84, 14525, '2021-09-01 20:39:19', '2021-09-01 23:26:09', 58576, 59936, 86.28);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (85, 14525, '2022-03-13 21:26:58', '2022-03-13 22:58:28', 59937, 60598, 67.17);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (86, 11285, '2024-07-31 09:57:27', '2024-07-31 11:48:27', 60325, 61784, 79.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (87, 11285, '2022-05-14 11:58:13', '2022-05-14 14:07:53', 60392, 61929, 61.23);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (91, 10226, '2024-10-13 23:05:13', '2024-10-13 23:54:43', 64301, 67155, 27.11);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (92, 10226, '2022-09-19 02:11:45', '2022-09-19 03:03:05', 64305, 67525, 28.18);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (93, 10226, '2022-01-13 04:37:40', '2022-01-13 05:44:40', 64300, 67967, 34.32);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (94, 10226, '2022-01-22 13:06:48', '2022-01-22 15:34:08', 64297, 71130, 45.95);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (95, 2375, '2023-08-28 16:03:18', '2023-08-28 18:19:38', 64302, 71274, 54.73);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (96, 10226, '2024-08-25 18:40:51', '2024-08-25 20:59:21', 64299, 71441, 89.93);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (97, 10226, '2021-10-27 04:38:19', '2021-10-27 07:06:09', 64303, 71437, 66.08);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (98, 10226, '2021-01-28 07:10:29', '2021-01-28 09:43:39', 64304, 71593, 98.22);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (99, 10226, '2024-04-27 10:16:06', '2024-04-27 13:29:26', 64306, 71814, 84.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (100, 2375, '2022-11-29 07:39:40', '2022-11-29 10:35:30', 64298, 71857, 96.06);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (101, 911, '2023-03-23 22:06:22', '2023-03-23 23:15:02', 72001, 74743, 32.38);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (102, 911, '2024-12-23 02:02:27', '2024-12-23 04:06:17', 71858, 76024, 88.96);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (103, 8070, '2024-12-13 10:14:57', '2024-12-13 11:53:27', 72585, 76867, 60.18);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (104, 12274, '2022-01-05 23:04:04', '2022-01-06 01:05:24', 72035, 76910, 69.37);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (105, 2815, '2023-01-16 00:25:27', '2023-01-16 02:29:37', 72163, 77044, 82.53);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (106, 13743, '2021-07-30 03:34:35', '2021-07-30 05:38:05', 72164, 77163, 79.67);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (107, 911, '2022-08-30 08:21:48', '2022-08-30 10:21:28', 72486, 77262, 82.12);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (108, 13743, '2023-05-18 20:10:10', '2023-05-18 22:17:20', 72333, 77298, 99.30);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (109, 4821, '2024-02-28 04:04:24', '2024-02-28 06:37:24', 77299, 78216, 98.85);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (110, 4821, '2023-06-28 01:51:23', '2023-06-28 03:31:13', 78217, 78815, 43.23);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (111, 4821, '2022-04-03 12:43:40', '2022-04-03 13:37:20', 78816, 79137, 27.52);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (112, 4821, '2024-06-20 19:19:21', '2024-06-20 22:58:31', 79138, 80452, 92.52);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (113, 4821, '2024-02-20 21:20:09', '2024-02-20 23:46:19', 80453, 81329, 98.06);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (114, 13354, '2021-02-23 17:39:58', '2021-02-23 19:35:38', 81330, 82023, 63.66);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (115, 13354, '2022-11-28 11:55:42', '2022-11-28 14:44:52', 82024, 83038, 75.51);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (116, 13354, '2024-02-16 19:25:33', '2024-02-16 20:49:53', 83039, 83544, 55.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (117, 13354, '2024-04-25 19:57:25', '2024-04-25 22:37:25', 83545, 84504, 99.20);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (118, 13354, '2021-03-29 14:35:25', '2021-03-29 16:52:25', 84505, 85326, 53.09);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (119, 7173, '2020-08-06 14:40:59', '2020-08-06 16:31:09', 85327, 85987, 88.21);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (120, 7173, '2023-03-07 18:13:50', '2023-03-07 20:45:20', 85988, 86896, 74.55);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (121, 7173, '2020-01-30 23:59:07', '2020-01-31 02:33:47', 86897, 87824, 62.50);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (122, 7173, '2024-07-02 01:53:43', '2024-07-02 03:33:03', 87825, 88420, 72.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (123, 7173, '2020-09-29 03:38:32', '2020-09-29 05:07:12', 88421, 88952, 75.57);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (124, 285, '2020-07-10 21:24:05', '2020-07-10 23:39:35', 88953, 89765, 86.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (125, 285, '2024-01-04 23:46:05', '2024-01-05 02:00:05', 89766, 90569, 54.19);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (126, 285, '2022-01-19 18:47:41', '2022-01-19 20:55:51', 90570, 91338, 41.66);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (127, 285, '2024-12-04 21:17:02', '2024-12-04 23:35:02', 91339, 92166, 82.00);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (128, 285, '2021-07-26 20:17:48', '2021-07-26 22:37:28', 92167, 93004, 56.45);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (129, 202, '2021-10-17 20:48:57', '2021-10-17 23:26:27', 93005, 93949, 82.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (130, 202, '2023-08-15 01:10:57', '2023-08-15 03:20:47', 93950, 94728, 91.47);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (131, 202, '2020-12-04 00:50:47', '2020-12-04 02:19:37', 94729, 95261, 47.82);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (132, 202, '2022-04-05 05:19:00', '2022-04-05 07:17:50', 95262, 95974, 87.39);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (133, 202, '2024-08-16 04:23:17', '2024-08-16 06:20:17', 95975, 96676, 63.51);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (134, 12775, '2023-12-28 23:31:58', '2023-12-29 00:27:38', 96677, 97010, 41.16);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (135, 12775, '2023-02-28 21:41:35', '2023-02-28 23:51:15', 97011, 97788, 57.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (136, 12775, '2020-05-23 14:32:00', '2020-05-23 16:11:10', 97789, 98383, 51.63);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (137, 12775, '2021-12-21 22:38:23', '2021-12-22 00:38:53', 98384, 99106, 58.34);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (138, 12775, '2023-06-12 23:35:32', '2023-06-13 01:09:02', 99107, 99667, 31.85);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (139, 9577, '2023-04-02 07:18:22', '2023-04-02 08:48:32', 99668, 100208, 46.29);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (140, 9577, '2021-08-16 19:02:29', '2021-08-16 21:09:19', 100209, 100969, 58.62);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (141, 9577, '2023-08-16 11:37:08', '2023-08-16 13:09:28', 100970, 101523, 60.25);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (142, 9577, '2023-05-14 07:46:41', '2023-05-14 09:35:11', 101524, 102174, 55.79);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (143, 9577, '2021-06-05 22:26:54', '2021-06-06 00:41:44', 102175, 102983, 81.49);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (144, 10669, '2021-02-18 07:02:41', '2021-02-18 09:23:21', 102984, 103827, 95.17);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (145, 10669, '2023-10-20 19:38:50', '2023-10-20 21:49:20', 103828, 104610, 46.53);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (146, 10669, '2023-11-17 09:06:45', '2023-11-17 11:06:05', 104611, 105326, 53.26);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (147, 10669, '2024-05-19 18:05:17', '2024-05-19 21:05:37', 105327, 106408, 96.32);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (148, 10669, '2020-01-25 10:34:09', '2020-01-25 11:45:29', 106409, 106836, 38.94);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (149, 13832, '2022-01-23 04:05:38', '2022-01-23 05:59:28', 106837, 107519, 43.09);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (150, 13832, '2024-09-13 15:12:22', '2024-09-13 17:34:52', 107520, 108374, 95.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (151, 13832, '2022-07-04 02:14:52', '2022-07-04 04:56:32', 108375, 109344, 87.73);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (152, 13832, '2021-11-08 13:13:33', '2021-11-08 15:50:33', 109345, 110286, 69.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (153, 13832, '2024-10-06 21:08:22', '2024-10-06 23:35:12', 110287, 111167, 73.50);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (154, 3087, '2020-10-23 09:27:14', '2020-10-23 11:17:34', 111168, 111829, 64.04);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (155, 3087, '2021-06-17 18:28:12', '2021-06-17 19:47:42', 111830, 112306, 47.09);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (156, 3087, '2022-02-21 20:54:13', '2022-02-21 21:58:03', 112307, 112689, 51.40);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (157, 3087, '2024-09-01 17:50:01', '2024-09-01 19:53:41', 112690, 113431, 74.19);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (158, 3087, '2023-02-06 07:07:20', '2023-02-06 08:20:00', 113432, 113867, 29.57);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (159, 11291, '2020-10-23 23:54:56', '2020-10-24 00:48:16', 113868, 114187, 33.33);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (160, 11291, '2024-08-15 01:22:54', '2024-08-15 03:39:34', 114188, 115007, 50.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (161, 11291, '2023-05-27 20:41:57', '2023-05-27 23:35:47', 115008, 116050, 94.28);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (162, 11291, '2023-02-12 15:56:12', '2023-02-12 18:14:12', 116051, 116878, 74.93);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (163, 11291, '2021-07-19 03:34:29', '2021-07-19 05:49:19', 116879, 117687, 58.21);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (164, 7885, '2023-05-11 12:50:13', '2023-05-11 13:49:33', 117688, 118043, 42.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (165, 7885, '2021-11-16 16:26:23', '2021-11-16 19:20:13', 118044, 119086, 79.62);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (166, 7885, '2024-04-06 02:30:42', '2024-04-06 04:48:02', 119087, 119910, 82.27);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (167, 7885, '2023-06-11 10:02:03', '2023-06-11 12:33:23', 119911, 120818, 55.76);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (168, 2548, '2020-05-10 03:53:30', '2020-05-10 05:54:00', 120819, 121541, 85.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (169, 2548, '2024-08-15 21:49:18', '2024-08-15 23:41:28', 121542, 122214, 76.11);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (170, 2548, '2024-01-18 14:04:58', '2024-01-18 16:34:48', 122215, 123113, 90.06);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (171, 2548, '2023-09-01 02:47:08', '2023-09-01 05:11:18', 123114, 123978, 95.59);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (172, 2548, '2020-04-20 07:14:38', '2020-04-20 09:50:38', 123979, 124914, 59.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (173, 14973, '2021-08-22 21:08:48', '2021-08-23 00:17:48', 124915, 126048, 95.63);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (174, 14973, '2020-06-28 22:38:20', '2020-06-29 00:49:00', 126049, 126832, 55.38);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (175, 14973, '2025-01-15 23:03:12', '2025-01-16 00:35:32', 126833, 127386, 63.28);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (176, 14973, '2023-08-29 09:28:59', '2023-08-29 12:06:19', 127387, 128330, 98.20);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (177, 14973, '2023-09-11 19:49:01', '2023-09-11 21:34:11', 128331, 128961, 64.73);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (178, 9745, '2021-07-04 00:28:36', '2021-07-04 01:41:46', 128962, 129400, 27.93);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (179, 9745, '2024-09-10 15:01:12', '2024-09-10 17:24:42', 129401, 130261, 52.37);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (180, 9745, '2022-02-16 21:29:47', '2022-02-16 22:40:27', 130262, 130685, 28.56);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (181, 9745, '2023-11-25 03:05:22', '2023-11-25 04:17:42', 130686, 131119, 33.77);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (182, 9745, '2021-04-03 06:02:51', '2021-04-03 07:49:31', 131120, 131759, 62.95);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (183, 2490, '2023-07-23 10:09:50', '2023-07-23 12:33:30', 131760, 132621, 68.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (184, 2490, '2023-11-03 14:05:58', '2023-11-03 16:24:08', 132622, 133450, 76.04);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (185, 2490, '2023-04-27 09:15:52', '2023-04-27 12:25:12', 133451, 134586, 73.78);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (186, 2490, '2024-10-15 18:13:18', '2024-10-15 19:40:18', 134587, 135108, 30.40);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (187, 2490, '2020-12-12 05:54:07', '2020-12-12 07:19:27', 135109, 135620, 44.44);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (188, 12216, '2021-04-23 07:32:09', '2021-04-23 09:36:59', 135621, 136369, 86.15);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (189, 12216, '2023-11-22 22:26:01', '2023-11-23 01:21:51', 136370, 137424, 82.97);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (190, 12216, '2020-04-25 15:51:02', '2020-04-25 17:05:52', 137425, 137873, 32.88);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (191, 12216, '2020-01-13 10:54:00', '2020-01-13 12:55:30', 137874, 138602, 50.32);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (192, 12216, '2023-12-03 12:53:24', '2023-12-03 15:56:24', 138603, 139700, 87.16);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (193, 2016, '2023-03-08 05:04:45', '2023-03-08 07:37:15', 139701, 140615, 89.55);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (194, 2016, '2020-11-18 00:20:01', '2020-11-18 01:50:01', 140616, 141155, 63.62);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (195, 2016, '2022-03-04 17:20:22', '2022-03-04 18:31:32', 141156, 141582, 33.06);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (196, 2016, '2020-08-22 13:03:19', '2020-08-22 14:51:59', 141583, 142234, 46.55);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (197, 2016, '2025-02-17 13:51:03', '2025-02-17 16:26:23', 142235, 143166, 76.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (198, 14229, '2021-05-24 02:50:28', '2021-05-24 04:06:08', 143167, 143620, 33.40);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (199, 14229, '2021-08-04 02:05:16', '2021-08-04 03:27:26', 143621, 144113, 31.25);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (200, 14229, '2025-01-06 05:37:29', '2025-01-06 08:25:29', 144114, 145121, 93.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (201, 14229, '2025-01-04 04:26:58', '2025-01-04 05:52:18', 145122, 145633, 28.73);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (202, 14229, '2023-12-05 19:17:07', '2023-12-05 22:17:27', 145634, 146715, 83.19);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (203, 172, '2022-04-26 10:55:03', '2022-04-26 14:06:13', 146716, 147862, 97.87);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (204, 172, '2021-06-06 21:44:10', '2021-06-06 23:35:40', 147863, 148531, 72.63);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (205, 172, '2023-02-27 08:21:45', '2023-02-27 10:00:15', 148532, 149122, 37.42);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (206, 172, '2021-02-13 01:25:48', '2021-02-13 03:49:18', 149123, 149983, 93.90);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (207, 172, '2023-07-03 10:13:53', '2023-07-03 13:00:33', 149984, 150983, 81.31);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (208, 5204, '2020-08-05 07:39:59', '2020-08-05 09:11:29', 150984, 151532, 43.77);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (209, 5204, '2022-11-06 00:02:12', '2022-11-06 01:39:42', 151533, 152117, 37.85);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (210, 5204, '2022-10-09 22:10:00', '2022-10-10 00:28:40', 152118, 152949, 93.39);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (211, 5204, '2022-04-18 15:52:59', '2022-04-18 18:16:59', 152950, 153813, 73.11);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (212, 5204, '2023-05-02 22:23:39', '2023-05-03 00:19:59', 153814, 154511, 71.28);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (213, 13017, '2023-07-14 08:52:50', '2023-07-14 10:23:00', 154512, 155052, 39.07);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (214, 13017, '2020-02-27 00:33:17', '2020-02-27 02:56:07', 155053, 155909, 98.43);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (215, 13017, '2022-03-07 11:47:12', '2022-03-07 14:15:42', 155910, 156800, 64.67);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (216, 13017, '2022-01-07 13:39:31', '2022-01-07 15:13:51', 156801, 157366, 66.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (217, 13017, '2022-02-20 06:22:06', '2022-02-20 08:12:36', 157367, 158029, 45.49);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (218, 12777, '2023-06-08 04:49:09', '2023-06-08 06:17:29', 158030, 158559, 39.87);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (219, 12777, '2024-03-13 10:04:57', '2024-03-13 12:29:07', 158560, 159424, 61.99);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (220, 12777, '2021-06-21 05:48:02', '2021-06-21 06:58:22', 159425, 159846, 29.99);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (221, 12777, '2022-06-07 18:07:20', '2022-06-07 20:03:00', 159847, 160540, 79.39);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (222, 12777, '2022-07-19 05:08:26', '2022-07-19 07:31:56', 160541, 161401, 68.70);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (223, 5173, '2022-08-28 03:44:44', '2022-08-28 06:05:14', 161402, 162244, 86.42);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (224, 5173, '2024-03-07 17:40:10', '2024-03-07 20:02:20', 162245, 163097, 94.20);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (225, 5173, '2021-05-06 21:24:30', '2021-05-07 00:20:40', 163098, 164154, 93.64);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (226, 5173, '2022-02-11 03:15:14', '2022-02-11 05:35:44', 164155, 164997, 98.19);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (227, 5173, '2022-06-23 07:38:36', '2022-06-23 09:24:16', 164998, 165631, 94.26);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (228, 14692, '2022-02-24 06:32:19', '2022-02-24 08:23:39', 165632, 166299, 77.32);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (229, 14692, '2020-09-13 14:21:02', '2020-09-13 17:13:12', 166300, 167332, 98.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (230, 14692, '2020-07-25 15:19:43', '2020-07-25 17:56:33', 167333, 168273, 63.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (231, 14692, '2022-10-29 06:05:37', '2022-10-29 08:59:37', 168274, 169317, 84.00);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (232, 14692, '2025-06-08 02:52:18', '2025-06-08 05:24:18', 169318, 170229, 57.51);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (233, 5586, '2024-06-09 17:26:30', '2024-06-09 19:55:50', 170230, 171125, 59.26);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (234, 5586, '2025-03-24 15:48:04', '2025-03-24 17:50:14', 171126, 171858, 55.42);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (235, 5586, '2023-01-03 07:13:50', '2023-01-03 08:29:40', 171859, 172313, 25.07);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (236, 5586, '2022-02-08 22:50:31', '2022-02-09 00:58:31', 172314, 173081, 51.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (237, 5586, '2022-01-17 16:44:59', '2022-01-17 17:40:19', 173082, 173413, 37.92);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (238, 10187, '2021-10-06 22:19:09', '2021-10-07 00:29:29', 173414, 174195, 56.23);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (239, 10187, '2024-09-12 07:01:05', '2024-09-12 08:46:45', 174196, 174829, 29.36);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (240, 10187, '2023-05-17 01:07:30', '2023-05-17 03:26:50', 174830, 175665, 57.31);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (241, 10187, '2024-08-30 15:34:22', '2024-08-30 18:05:12', 175666, 176570, 57.95);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (242, 10187, '2022-07-15 23:37:09', '2022-07-16 01:11:39', 176571, 177137, 54.51);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (243, 14184, '2025-06-22 17:35:22', '2025-06-22 19:17:42', 177138, 177751, 46.20);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (244, 14184, '2024-02-20 08:48:40', '2024-02-20 09:59:00', 177752, 178173, 29.75);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (245, 14184, '2023-10-18 20:05:39', '2023-10-18 22:40:29', 178174, 179102, 54.90);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (246, 14184, '2021-03-17 23:11:19', '2021-03-18 00:43:49', 179103, 179657, 29.13);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (247, 14184, '2020-10-09 10:13:57', '2020-10-09 11:40:17', 179658, 180175, 40.16);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (248, 6336, '2022-05-12 11:39:24', '2022-05-12 12:53:44', 180176, 180621, 32.66);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (249, 6336, '2021-03-03 20:30:28', '2021-03-03 22:08:18', 180622, 181208, 38.74);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (250, 6336, '2023-05-31 14:38:15', '2023-05-31 16:40:05', 181209, 181939, 59.40);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (251, 6336, '2020-07-05 18:54:44', '2020-07-05 19:56:54', 181940, 182312, 28.53);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (252, 6336, '2023-06-15 16:01:54', '2023-06-15 19:05:04', 182313, 183411, 87.15);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (253, 1487, '2021-08-10 13:28:12', '2021-08-10 14:42:32', 183412, 183857, 51.77);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (254, 1487, '2023-02-15 22:55:25', '2023-02-16 00:59:55', 183858, 184604, 52.82);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (255, 1487, '2021-06-13 10:49:00', '2021-06-13 13:20:10', 184605, 185511, 75.98);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (256, 1487, '2021-08-16 21:15:34', '2021-08-16 23:20:34', 185512, 186261, 69.88);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (257, 1487, '2023-03-01 23:28:18', '2023-03-02 01:13:08', 186262, 186890, 53.45);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (258, 2842, '2021-01-27 09:17:51', '2021-01-27 11:52:11', 186891, 187816, 70.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (259, 2842, '2021-11-09 07:56:40', '2021-11-09 09:47:20', 187817, 188480, 46.85);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (260, 2842, '2020-09-05 15:39:40', '2020-09-05 17:25:00', 188481, 189112, 61.39);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (261, 2842, '2022-05-04 23:57:33', '2022-05-05 01:43:23', 189113, 189747, 34.92);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (262, 2842, '2021-07-14 10:39:23', '2021-07-14 12:31:13', 189748, 190418, 60.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (263, 3722, '2020-11-14 09:13:30', '2020-11-14 12:09:20', 190419, 191473, 83.34);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (264, 3722, '2025-03-13 16:23:37', '2025-03-13 17:55:07', 191474, 192022, 51.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (265, 3722, '2020-03-24 12:49:27', '2020-03-24 13:50:07', 192023, 192386, 44.83);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (266, 3722, '2020-04-19 19:03:07', '2020-04-19 21:36:17', 192387, 193305, 75.74);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (267, 3722, '2024-01-15 10:14:33', '2024-01-15 11:52:43', 193306, 193894, 40.15);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (268, 13783, '2022-10-05 21:37:35', '2022-10-05 22:48:45', 193895, 194321, 31.58);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (269, 13783, '2022-01-28 02:30:11', '2022-01-28 05:23:51', 194322, 195363, 94.55);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (270, 13783, '2021-11-01 07:17:38', '2021-11-01 09:21:48', 195364, 196108, 53.80);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (271, 13783, '2020-11-03 18:38:17', '2020-11-03 19:58:37', 196109, 196590, 31.08);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (272, 13783, '2020-01-05 21:51:38', '2020-01-05 23:28:58', 196591, 197174, 71.60);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (273, 6184, '2021-03-16 01:03:30', '2021-03-16 02:58:30', 197175, 197864, 97.13);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (274, 6184, '2024-09-29 15:52:56', '2024-09-29 16:56:46', 197865, 198247, 60.69);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (275, 6184, '2021-05-31 05:36:08', '2021-05-31 07:23:48', 198248, 198893, 68.75);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (276, 6184, '2021-07-23 21:10:50', '2021-07-23 23:44:00', 198894, 199812, 97.88);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (277, 6184, '2024-07-31 10:12:58', '2024-07-31 12:20:58', 199813, 200580, 90.21);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (278, 8318, '2024-03-18 03:49:26', '2024-03-18 06:56:36', 200581, 201703, 96.66);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (279, 8318, '2023-12-27 23:05:08', '2023-12-28 00:56:48', 201704, 202373, 78.80);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (280, 8318, '2022-11-11 18:10:20', '2022-11-11 21:23:30', 202374, 203532, 94.19);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (281, 8318, '2025-03-05 23:56:42', '2025-03-06 02:42:42', 203533, 204528, 98.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (282, 8318, '2022-10-07 17:58:35', '2022-10-07 19:59:15', 204529, 205252, 61.10);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (283, 261, '2021-06-14 16:54:51', '2021-06-14 18:48:11', 205253, 205932, 74.43);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (284, 261, '2023-04-04 14:02:23', '2023-04-04 15:40:43', 205933, 206522, 62.14);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (285, 261, '2021-10-09 21:26:46', '2021-10-09 23:14:56', 206523, 207171, 38.46);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (286, 261, '2022-03-28 06:41:11', '2022-03-28 08:06:51', 207172, 207685, 32.44);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (287, 261, '2022-09-08 02:48:14', '2022-09-08 06:24:04', 207686, 208980, 93.07);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (288, 5506, '2020-11-09 07:01:29', '2020-11-09 09:19:09', 208981, 209806, 76.76);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (289, 5506, '2022-10-28 04:23:27', '2022-10-28 05:39:57', 209807, 210265, 29.69);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (290, 5506, '2022-11-24 03:48:12', '2022-11-24 05:52:12', 210266, 211009, 78.33);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (291, 5506, '2022-07-24 21:55:50', '2022-07-24 23:26:10', 211010, 211551, 46.59);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (292, 5506, '2021-03-08 15:52:58', '2021-03-08 17:01:18', 211552, 211961, 34.65);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (293, 11237, '2020-08-24 18:11:01', '2020-08-24 20:08:01', 211962, 212663, 73.23);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (294, 11237, '2024-03-26 07:22:05', '2024-03-26 09:11:25', 212664, 213319, 40.42);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (295, 11237, '2021-10-10 16:51:26', '2021-10-10 20:00:36', 213320, 214454, 93.82);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (296, 11237, '2022-04-05 09:19:05', '2022-04-05 11:00:55', 214455, 215065, 38.16);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (297, 11237, '2025-06-16 06:18:57', '2025-06-16 08:32:17', 215066, 215865, 64.18);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (298, 3155, '2022-08-12 22:53:00', '2022-08-13 00:56:40', 215866, 216607, 58.63);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (299, 3155, '2024-09-21 00:51:11', '2024-09-21 01:32:31', 216608, 216855, 28.75);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (300, 3155, '2023-09-20 00:56:03', '2023-09-20 01:57:53', 216856, 217226, 29.15);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (301, 3155, '2024-12-06 03:18:58', '2024-12-06 05:42:08', 217227, 218085, 64.71);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (302, 3155, '2024-11-11 23:42:20', '2024-11-12 00:20:20', 218086, 218313, 27.03);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (303, 9232, '2021-12-01 22:48:49', '2021-12-02 00:38:09', 218314, 218969, 27.97);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (304, 9232, '2023-08-27 07:19:33', '2023-08-27 08:06:13', 218970, 219249, 33.08);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (305, 9232, '2024-12-03 17:06:05', '2024-12-03 18:51:45', 219250, 219883, 67.03);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (306, 9232, '2022-09-30 21:20:51', '2022-09-30 23:08:01', 219884, 220526, 94.15);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (307, 9232, '2021-05-02 19:05:30', '2021-05-02 21:42:20', 220527, 221467, 59.07);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (308, 13236, '2021-02-17 22:26:55', '2021-02-18 00:12:45', 221468, 222102, 70.96);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (309, 13236, '2021-10-31 02:50:38', '2021-10-31 04:35:38', 222103, 222732, 41.52);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (310, 13236, '2021-12-07 22:51:37', '2021-12-08 01:56:57', 222733, 223844, 94.06);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (311, 13236, '2021-03-29 18:13:49', '2021-03-29 19:39:49', 223845, 224360, 50.62);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (312, 13236, '2020-02-24 16:30:52', '2020-02-24 19:27:02', 224361, 225417, 97.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (313, 13236, '2021-11-03 15:07:48', '2021-11-03 17:21:38', 225418, 226220, 86.62);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (314, 13236, '2024-03-27 05:51:19', '2024-03-27 06:23:19', 226221, 226412, 25.07);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (315, 13236, '2020-11-25 05:22:09', '2020-11-25 08:08:39', 226413, 227411, 88.81);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (316, 13236, '2021-02-07 13:03:54', '2021-02-07 16:01:14', 227412, 228475, 96.58);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (317, 10881, '2020-05-16 01:27:59', '2020-05-16 03:16:19', 228476, 229125, 49.25);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (318, 10881, '2021-01-16 19:51:32', '2021-01-16 21:48:52', 229126, 229829, 58.05);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (319, 10881, '2024-12-07 12:22:34', '2024-12-07 13:35:14', 229830, 230265, 55.71);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (320, 10881, '2025-05-01 16:01:04', '2025-05-01 18:24:04', 230266, 231123, 77.06);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (321, 10881, '2023-06-19 00:50:43', '2023-06-19 02:52:23', 231124, 231853, 53.36);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (322, 11823, '2020-02-20 08:52:20', '2020-02-20 10:54:00', 231854, 232583, 82.58);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (323, 11823, '2022-04-18 19:42:35', '2022-04-18 21:12:15', 232584, 233121, 34.77);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (324, 11823, '2022-09-28 09:32:41', '2022-09-28 11:34:41', 233122, 233853, 54.31);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (325, 11823, '2020-08-12 20:18:27', '2020-08-12 21:18:47', 233854, 234215, 24.54);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (326, 11823, '2024-11-12 17:39:17', '2024-11-12 19:10:37', 234216, 234763, 72.45);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (327, 12060, '2022-03-09 15:51:56', '2022-03-09 18:29:56', 234764, 235711, 91.12);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (328, 12060, '2023-03-21 23:14:08', '2023-03-22 01:18:28', 235712, 236457, 59.25);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (329, 12060, '2021-01-22 15:24:35', '2021-01-22 17:03:45', 236458, 237052, 55.73);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (330, 12060, '2024-01-21 08:48:23', '2024-01-21 09:51:03', 237053, 237428, 44.95);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (331, 12060, '2021-06-12 07:49:52', '2021-06-12 09:16:32', 237429, 237948, 44.05);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (332, 81, '2025-07-02 22:14:51', '2025-07-03 00:31:11', 237949, 238766, 87.12);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (333, 81, '2021-06-25 13:29:33', '2021-06-25 15:18:23', 238767, 239419, 61.62);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (334, 81, '2022-12-24 19:23:12', '2022-12-24 22:02:02', 239420, 240372, 56.69);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (335, 81, '2023-07-05 23:15:09', '2023-07-06 01:32:19', 240373, 241195, 70.75);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (336, 81, '2023-02-10 12:31:15', '2023-02-10 14:40:05', 241196, 241968, 62.42);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (337, 4388, '2020-04-05 14:17:10', '2020-04-05 16:01:40', 241969, 242595, 52.69);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (338, 4388, '2021-09-15 13:27:45', '2021-09-15 16:43:05', 242596, 243767, 77.14);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (339, 4388, '2023-01-02 06:27:24', '2023-01-02 07:56:04', 243768, 244299, 44.26);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (340, 4388, '2021-05-17 13:10:57', '2021-05-17 15:29:17', 244300, 245129, 75.36);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (341, 4388, '2020-02-07 08:47:14', '2020-02-07 10:25:44', 245130, 245720, 75.75);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (342, 12952, '2021-01-11 22:47:59', '2021-01-12 01:17:19', 245721, 246616, 72.76);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (343, 12952, '2025-02-24 22:51:10', '2025-02-25 00:14:40', 246617, 247117, 45.51);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (344, 12952, '2023-05-27 17:25:08', '2023-05-27 19:05:08', 247118, 247717, 42.02);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (345, 12952, '2023-12-25 13:17:20', '2023-12-25 15:03:10', 247718, 248352, 53.51);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (346, 12952, '2025-03-28 05:31:34', '2025-03-28 08:52:34', 248353, 249558, 91.91);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (347, 13150, '2022-05-03 13:30:23', '2022-05-03 15:56:23', 249559, 250434, 96.77);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (348, 13150, '2024-09-29 15:27:51', '2024-09-29 17:35:01', 250435, 251197, 69.40);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (349, 13150, '2020-01-24 09:00:57', '2020-01-24 10:39:47', 251198, 251790, 92.79);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (350, 13150, '2021-07-08 18:37:58', '2021-07-08 20:51:48', 251791, 252593, 80.10);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (351, 13150, '2024-07-13 05:33:02', '2024-07-13 08:04:52', 252594, 253504, 97.15);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (352, 5913, '2025-03-16 01:12:23', '2025-03-16 03:37:33', 253505, 254375, 97.04);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (353, 5913, '2021-04-14 09:38:47', '2021-04-14 11:25:57', 254376, 255018, 50.27);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (354, 5913, '2020-03-17 11:05:23', '2020-03-17 12:03:13', 255019, 255365, 27.78);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (355, 5913, '2023-02-21 19:32:16', '2023-02-21 21:12:56', 255366, 255969, 47.40);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (356, 10864, '2020-07-04 13:02:19', '2020-07-04 14:46:39', 255970, 256595, 37.33);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (357, 10864, '2024-04-09 21:21:11', '2024-04-09 22:58:11', 256596, 257177, 41.91);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (358, 10864, '2022-12-04 02:36:51', '2022-12-04 04:13:31', 257178, 257757, 44.54);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (359, 10864, '2024-11-01 13:16:42', '2024-11-01 14:59:02', 257758, 258371, 54.19);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (360, 13867, '2021-10-29 21:38:32', '2021-10-29 22:47:42', 258372, 258786, 34.10);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (361, 13867, '2020-04-05 08:54:48', '2020-04-05 11:49:48', 258787, 259836, 72.87);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (362, 13867, '2023-10-22 16:16:43', '2023-10-22 17:25:33', 259837, 260249, 56.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (363, 9601, '2021-01-12 12:19:57', '2021-01-12 13:34:17', 260250, 260695, 44.50);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (364, 9601, '2024-10-02 23:26:19', '2024-10-03 02:00:59', 260696, 261623, 98.73);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (365, 9601, '2021-11-09 14:50:59', '2021-11-09 16:24:29', 261624, 262184, 47.10);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (366, 9601, '2024-01-06 19:23:56', '2024-01-06 21:31:26', 262185, 262949, 59.90);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (367, 12719, '2022-01-30 22:30:49', '2022-01-31 00:42:29', 262950, 263739, 72.25);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (368, 13026, '2022-12-31 13:49:59', '2022-12-31 14:51:39', 263740, 264109, 40.66);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (369, 14209, '2023-09-22 07:18:34', '2023-09-22 09:43:14', 264110, 264977, 88.12);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (370, 4015, '2021-01-22 21:09:58', '2021-01-22 23:21:28', 264978, 265766, 62.44);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (371, 6260, '2023-10-18 01:14:41', '2023-10-18 02:37:31', 265767, 266263, 45.24);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (372, 10139, '2025-03-14 19:10:12', '2025-03-14 21:31:42', 266264, 267112, 52.44);
--INSERT INTO autopark.trip (id, vehicle_id, start_time, end_time, start_gps_data_id, end_gps_data_id, mileage) VALUES (382, 15057, '2025-05-10 15:25:00', '2025-05-10 15:43:00', 275618, 276562, 19.79);

-- Или если используете PostGIS синтаксис:
--INSERT INTO autopark.gps_data (vehicle_id, coordinates, timestamp)
--VALUES (1, ST_GeomFromText('POINT(-74.0060 40.7128)', 4326), '2024-12-14 10:00:00');

--INSERT INTO autopark.gps_data (vehicle_id, coordinates, timestamp)
--VALUES
--(1, 'SRID=4326;POINT(-74.0060 40.7128)', '2024-12-14 10:00:00');