#!/bin/sh
set -e

# Use Actuator endpoint for health check
ACTUATOR_HEALTH_URL="http://localhost:9001/actuator/health"

# Curl with fail-fast behavior and silent output
curl --fail --silent --show-error --connect-timeout 1 --max-time 2 "$ACTUATOR_HEALTH_URL"
