#!/bin/sh
set -e

echo "✔ Starting Spring Boot container..."

echo "✔ Server Port: ${SERVER_PORT:-9000}"
echo "✔ Management Port: ${MANAGEMENT_PORT:-9001}"
echo "✔ Active Profile(s): ${SPRING_PROFILES_ACTIVE:-default}"

echo "🚀 Launching Spring Boot app..."
exec "$@"
