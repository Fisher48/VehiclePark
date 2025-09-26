#!/bin/bash

set -e  # Остановить скрипт при ошибке

# 📌 Папка, где будет развертываться проект
DEPLOY_DIR="$HOME/vehiclepark_deploy_local"

echo "🚀 Начинаем локальный деплой в $DEPLOY_DIR"

# 1 Останавливаем старые контейнеры, если они есть
if [ -d "$DEPLOY_DIR" ]; then
    echo "🛑 Останавливаем старые контейнеры..."
    cd "$DEPLOY_DIR"
    docker-compose down || true  # Останавливаем, но не выходим при ошибке
else
    mkdir -p "$DEPLOY_DIR"
fi

# 2 Копируем свежие файлы
echo "📂 Копируем файлы проекта..."
rsync -av --exclude='.git' --exclude='node_modules' --exclude='target' . "$DEPLOY_DIR/"

# 3 Переходим в папку и запускаем сервисы
cd "$DEPLOY_DIR"
echo "🐳 Запускаем контейнеры..."
docker-compose up -d --build

# 4 Проверяем статус
echo "✅ Деплой завершен!"
docker ps