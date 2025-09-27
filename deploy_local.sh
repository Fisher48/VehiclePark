#!/bin/bash
set -e

DEPLOY_DIR="$HOME/vehiclepark_deploy_local"

echo "🚀 Начинаем локальный деплой в $DEPLOY_DIR"

# 1. Останавливаем старые контейнеры
if [ -d "$DEPLOY_DIR" ]; then
    echo "🛑 Останавливаем старые контейнеры..."
    cd "$DEPLOY_DIR"
    docker-compose down || true
else
    mkdir -p "$DEPLOY_DIR"
fi

# 2. Копируем файлы проекта (но оставляем target!)
echo "📂 Копируем файлы проекта..."
rsync -av --exclude='.git' --exclude='node_modules' . "$DEPLOY_DIR/"

# 3. Собираем проект в папке деплоя
cd "$DEPLOY_DIR"
echo "🔨 Собираем проект..."
mvn clean package -DskipTests

# 4. Запускаем контейнеры
echo "🐳 Запускаем контейнеры..."
docker-compose up -d --build

echo "✅ Деплой завершен!"
docker ps