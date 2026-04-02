#!/bin/bash
set -e

# Check if docker compose (v2) or docker-compose (v1) is available
if docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
elif command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
else
    echo "Error: Docker Compose is not installed."
    echo "Please install Docker and Docker Compose to run this application."
    exit 1
fi

echo "Starting the Note App with $COMPOSE_CMD..."
$COMPOSE_CMD up --build
