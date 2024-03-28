#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE accountant;
	GRANT ALL PRIVILEGES ON DATABASE accountant TO postgres;
EOSQL