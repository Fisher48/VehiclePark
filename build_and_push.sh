#!/bin/bash
set -e

# === Настройки ===
REGISTRY_USER="fisher48"           # логин в Docker Hub
TAG="latest"                       # можно поменять на версию, например 1.0.0
SERVICES=("vehiclepark-core" "notification-service" "telegram-bot-service")

# === 1. Сборка всех микросервисов через Maven ===
echo "🛠️  Сборка Maven-проектов..."
mvn clean package -DskipTests

# === 2. Сборка Docker-образов ===
for SERVICE in "${SERVICES[@]}"; do
  echo ""
  echo "🐳 Сборка Docker-образа для: ${SERVICE}"
  docker build -t "${SERVICE}:latest" -f "${SERVICE}/Dockerfile" .
done

# === 3. Вход в Docker Hub ===
echo ""
echo "🔐 Вход в Docker Hub..."
docker login

# === 4. Тегирование и публикация образов ===
for SERVICE in "${SERVICES[@]}"; do
  IMAGE_NAME="${REGISTRY_USER}/${SERVICE}:${TAG}"
  echo ""
  echo "🚀 Публикация ${IMAGE_NAME}..."
  docker tag "${SERVICE}:latest" "${IMAGE_NAME}"
  docker push "${IMAGE_NAME}"
done

# === 5. Проверка ===
echo ""
echo "✅ Проверка образов..."
for SERVICE in "${SERVICES[@]}"; do
  IMAGE_NAME="${REGISTRY_USER}/${SERVICE}:${TAG}"
  docker pull "${IMAGE_NAME}" > /dev/null
  echo "✔️  ${IMAGE_NAME} доступен в Docker Hub"
done

echo ""
echo "🎉 Все образы успешно собраны и опубликованы!"