#!/bin/bash
set -e

function createdb_if_not_exists {
 if [ "$( psql --username "${POSTGRES_USER}" --dbname postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$1'" )" = '1' ]
 then
   echo "Database $1 already exists"
 else
   psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
      CREATE DATABASE $1;
      GRANT ALL PRIVILEGES ON DATABASE $1 TO postgres;
   EOSQL
 fi
}

createdb_if_not_exists accountant



