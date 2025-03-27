#!/bin/bash

set -e  # Останавливаем скрипт при ошибке

# 🔹 ДАННЫЕ ДЛЯ ПОДКЛЮЧЕНИЯ
SERVER_USER="root"   # <-- Укажи пользователя на сервере
SERVER_HOST="89.111.152.238" # <-- Укажи IP или домен сервера
SERVER_DIR="/VehiclePark"  # Папка на сервере

echo "🚀 Деплой на $SERVER_HOST в $SERVER_DIR"

# 1 Копируем файлы проекта на сервер
echo "📂 Копируем файлы на сервер..."
rsync -av --exclude='.git' --exclude='node_modules' --exclude='target' . "$SERVER_USER@$SERVER_HOST:$SERVER_DIR/"

# 2 Подключаемся по SSH и перезапускаем сервисы
echo "🐳 Перезапускаем контейнеры на сервере..."
ssh "$SERVER_USER@$SERVER_HOST" <<EOF
  cd "$SERVER_DIR"
  docker-compose down || true
  docker-compose up -d --build
EOF

echo "✅ Деплой завершен!"