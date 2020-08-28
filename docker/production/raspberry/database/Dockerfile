FROM postgres:12-alpine

COPY init-user-db.sh /docker-entrypoint-initdb.d/init-user-db.sh

RUN dos2unix /docker-entrypoint-initdb.d/init-user-db.sh