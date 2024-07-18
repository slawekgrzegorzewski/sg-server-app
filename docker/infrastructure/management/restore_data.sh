CONTAINER_ID=$(docker ps -f name=application_database --quiet)
docker exec -it $CONTAINER_ID psql -h localhost -p 5432 -U postgres -w postgres -c 'DROP DATABASE accountant;'
docker exec -it $CONTAINER_ID psql -h localhost -p 5432 -U postgres -w postgres -c 'CREATE DATABASE accountant;'
cp $1 $HOME/Application/docker/volumes/database_backup/backup.sql
docker exec -it $CONTAINER_ID psql accountant -h localhost -U postgres -f /backup/backup.sql
