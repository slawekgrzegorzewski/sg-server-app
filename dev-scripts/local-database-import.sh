#!/bin/bash
#scp -r slag@grzegorzewski.org:/home/slag/sg-application/database-backups /Users/slag/Projects/database-backups

set PGPASSWORD=SLAwek1!
file=$(ls /Users/slag/Projects/database-backups/current/)
echo $file``
psql -h localhost -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
psql -h localhost -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
psql -h localhost -U postgres -w accountant < "/Users/slag/Projects/database-backups/current/$file"