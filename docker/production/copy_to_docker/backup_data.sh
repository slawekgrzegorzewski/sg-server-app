FILE_NAME=/home/slawek/management/database-backups/database_`date +"%Y-%m-%d_%H-%M-%S"`.sql
pg_dump accountant -h localhost -U postgres > $FILE_NAME
gzip -9 $FILE_NAME