# **VehiclePark** 🚗
___
📖 Описание:  
VehiclePark — это система для управления автопарком предприятий с водителями и автомобилями, 
позволяет создавать, изменять автомобили, отслеживать поездки и выгружать отчеты о пробегах.

**_Основные возможности:_**
* 🔐 Поддержки двух видов авторизации:  
[Классическая (через форму входа) для веб-интерфейса](#login) и JWT-токены для REST API
* 🏢 [Учёт предприятий и их менеджеров](#enterprises)
* 🚘 [Управление парком автомобилей](#cars)
* 🛰️ [Хранения GPS-данных и построения треков поездок на карте](#map)
* 📊 [Выгрузка отчета о пробеге автомобилей](#report)
* 📝 [Получение запросов по REST c JWT](#rest) 
* 🤖 [Отправки уведомлений в Telegram-бота и на почту](#tg-bot)
* ℹ️ [Документация по основным конечным точкам - Swagger](#swagger)
* 🚀 [Запуск проекта](#deploy)

___
🛠️ _**Технологический стек:**_  
Backend: Spring Boot 3.3.5, Spring Security, Spring Data JPA, Spring Kafka  
Database: PostgreSQL 16+ with PostGIS для геоданных  
Routing: OpenRouteService  
Message Broker: Apache Kafka  
Monitoring: Prometheus, Grafana, Spring Boot Actuator  
Containerization: Docker, Docker Compose   
Tests: JUnit, Mockito, Cypress, Playwright  
Load Testing: k6, wrk  
Telegram Integration: Telegram Bot API  
___
🏗 _**Архитектура проекта:**_  
Проект реализован в виде 3-х микросервисов, которые взаимодействуют через Kafka:

* vehiclepark-core — основной сервис (менеджеры, предприятия, водители, авто, GPS, REST API)
* notification-service — принимает события и отправляет их в Kafka
* telegram-bot-service — отдельный микросервис для уведомлений в Telegram
* Prometheus + Grafana + Alertmanager — мониторинг, метрики, алерты на почту и в телеграмм
* PostgreSQL (PostGIS) — основная база данных
* NGINX — прокси
___
📸 Скриншоты  

<a name="login"></a>
👥 **_Авторизация менеджера_**  
<img alt="login.png" height="400" src="images/login.png" width="800"/>

<a name="enterprises"></a>
🏢 **_Список предприятий менеджера_**  
<img alt="enterprises_list.png" height="500" src="images/enterprises_list.png" width="800"/>

<a name="cars"></a>
🚘 _**Список машин предприятия**_  
<img alt="vehicles_list.png" height="500" src="images/vehicles_list.png" width="800"/>

🚗 _**Просмотр информации о машине**_  
<img alt="vehicle_info.png" height="250" src="images/vehicle_info.png" width="800"/>

<a name="map"></a>
🛰️ _**Отображение треков автомобилей на карте**_  
<img alt="trips.png" height="500" src="images/trips.png" width="1000"/>
<img alt="map.png" height="500" src="images/map.png" width="1000"/>

<a name="report"></a>
📊 _**Выгрузка отчета о пробеге автомобиля за определенный период**_
<img alt="report_form.png" height="350" src="images/report_form.png" width="900"/>
<img alt="report.png" height="400" src="images/report.png" width="900"/>

<a name="rest"></a>
📝 **_Получение запросов по REST c JWT_**
<img alt="rest_api.png" height="600" src="images/rest_api.png" width="800"/>

<a name="tg-bot"></a>
🤖 _**Работа с телеграм-ботом**_  
<img alt="work_with_tg_bot.png" height="600" src="images/work_with_tg_bot.png" width="700"/>
<img alt="telegram-bot.png" height="600" src="images/telegram-bot.png" width="700"/>  

<a name="swagger"></a>
ℹ️ _**REST API (Swagger UI)**_  
<img alt="swagger.png" height="600" src="images/swagger.png" width="900"/>
___
<a name="deploy"></a>
🚀 **_Запуск проекта_**
1. Клонирование репозитория  
   `git clone https://github.com/Fisher48/VehiclePark.git`  
   `cd vehiclepark`


2. Запуск проекта (2 варианта)
   1. Через Docker Compose: `docker compose up -d --build`   
   2. Через файл `deploy_local.sh`  
   Даем права на исполнение файлу: `chmod +x deploy_local.sh`  
   Запускаем скрипт: .`/deploy_local.sh` или `bash deploy_local.sh`


3. Доступ к сервисам:

📊 Grafana: http://localhost:3000
(логин/пароль: admin/admin)  
🔍 Prometheus: http://localhost:9090  
🚨 Alertmanager: http://localhost:9093  
⚙️ Swagger UI: http://localhost/api/swagger-ui.html
🤖 Telegram-бот: доступен по токену (подключите в Alertmanager)  

📦 Docker Hub  
Можно загрузить готовый образ:

docker pull fisher48/vehiclepark:latest