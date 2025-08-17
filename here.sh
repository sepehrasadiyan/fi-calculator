#!/usr/bin/env bash
set -euo pipefail
echo -e "This bash help you to start with minimum commands to run hole project :) \n"
if [ ! -f .env ]; then
  echo "creating .env with defaults"
  touch .env
fi

if ! grep -qE '^DB_HOST=' .env && [ -z "${DB_HOST:-}" ]; then echo "DB_HOST=db" >> .env; fi
if ! grep -qE '^DB_PORT=' .env && [ -z "${DB_PORT:-}" ]; then echo "DB_PORT=5432" >> .env; fi
if ! grep -qE '^DB_NAME=' .env && [ -z "${DB_NAME:-}" ]; then echo "DB_NAME=fi-calc" >> .env; fi
if ! grep -qE '^DB_USER=' .env && [ -z "${DB_USER:-}" ]; then echo "DB_USER=fi-user" >> .env; fi
if ! grep -qE '^DB_PASSWORD=' .env && [ -z "${DB_PASSWORD:-}" ]; then echo "DB_PASSWORD=fi-pass" >> .env; fi
if ! grep -qE '^SERVER_PORT=' .env && [ -z "${SERVER_PORT:-}" ]; then echo "SERVER_PORT=8080" >> .env; fi

docker compose build app

# if you want add -d to it.
docker compose up app