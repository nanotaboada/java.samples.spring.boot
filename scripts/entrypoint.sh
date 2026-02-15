#!/bin/sh
set -e

echo "✔ Executing entrypoint script..."

IMAGE_STORAGE_PATH="/app/hold/players-sqlite3.db"
VOLUME_STORAGE_PATH="/storage/players-sqlite3.db"

echo "✔ Starting container..."

if [ ! -f "$VOLUME_STORAGE_PATH" ]; then
  echo "⚠️ No existing database file found in volume."
  if [ -f "$IMAGE_STORAGE_PATH" ]; then
    echo "Copying database file to writable volume..."
    if cp "$IMAGE_STORAGE_PATH" "$VOLUME_STORAGE_PATH"; then
      echo "✔ Database initialized at $VOLUME_STORAGE_PATH"
    else
      echo "❌ Failed to copy database from $IMAGE_STORAGE_PATH to $VOLUME_STORAGE_PATH"
      echo "   Check file permissions and available disk space."
      exit 1
    fi
  else
    echo "⚠️ Database file missing at $IMAGE_STORAGE_PATH"
    exit 1
  fi
else
  echo "✔ Existing database file found. Skipping seed copy."
fi

echo "✔ Ready!"
echo "🚀 Launching app..."
echo ""
echo "🔌 Endpoints:"
echo "   Health:  http://localhost:9001/actuator/health"
echo "   Players: http://localhost:9000/players"
echo ""
exec "$@"
