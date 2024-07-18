CONTAINER_ID=$(docker ps -f name=application_database --quiet)
FILE_NAME=database_`date +"%Y-%m-%d_%H-%M-%S"`.sql
docker exec $CONTAINER_ID pg_dump accountant -h localhost -U postgres -f /backup/$FILE_NAME
gzip -9 -c $HOME/Application/docker/volumes/database_backup/$FILE_NAME > $HOME/Application/management/database-backups/$FILE_NAME.gz
rm -f $HOME/Application/docker/volumes/database_backup/$FILE_NAME