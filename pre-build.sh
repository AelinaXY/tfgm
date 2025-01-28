#!/bin/sh
docker compose up -d postgres-db mongo-db
docker compose run wait-for-db

