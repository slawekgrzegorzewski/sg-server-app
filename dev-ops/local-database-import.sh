#!/bin/bash
#scp -r slag@grzegorzewski.org:/home/slag/sg-application/database-backups /Users/slag/Projects/database-backups

file=$(ls ~/Development/Slawek/backups/current/)

PGPASSWORD=SLAwek1!

echo "*:*:*:postgres:SLAwek1!" | tee ~/.pgpass
chmod 400 ~/.pgpass

 echo "$HOME/Development/Slawek/backups/current/$file"

#psql -h localhost -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
#psql -h localhost -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
#psql -h localhost -U postgres -w accountant < "$HOME/Development/Slawek/backups/current/$file"
psql -h 192.168.52.98 -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
psql -h 192.168.52.98 -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
psql -h 192.168.52.98 -U postgres -w accountant < "$HOME/Development/Slawek/backups/current/$file"