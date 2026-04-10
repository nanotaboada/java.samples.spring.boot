#!/bin/sh
set -e

# Helper function for formatted logging
log() {
    echo "[ENTRYPOINT] $(date '+%Y/%m/%d - %H:%M:%S') | $1"
    return 0
}

log "✔ Starting container..."

STORAGE_PATH="${STORAGE_PATH:-storage/players-sqlite3.db}"

mkdir -p "$(dirname "$STORAGE_PATH")"

if [ ! -w "$(dirname "$STORAGE_PATH")" ]; then
    log "❌ Storage directory is not writable: $(dirname "$STORAGE_PATH")"
    exit 1
fi

if [ -f "$STORAGE_PATH" ] && [ ! -w "$STORAGE_PATH" ]; then
    log "❌ Database file is not writable: $STORAGE_PATH"
    exit 1
fi

if [ ! -f "$STORAGE_PATH" ]; then
    log "⚠️ No existing database file found at $STORAGE_PATH."
    log "🗄️ Flyway migrations will initialize the database on first start."
else
    log "✔ Existing database file found at $STORAGE_PATH."
fi

log "✔ Ready!"
log "🚀 Launching app..."
log "🔌 API endpoints   | http://localhost:9000"
log "📚 Swagger UI      | http://localhost:9000/swagger/index.html"
log "❤️ Health check    | http://localhost:9001/actuator/health"
exec "$@"
