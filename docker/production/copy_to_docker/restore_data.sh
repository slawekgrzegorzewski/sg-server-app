psql -h localhost -p 5432 -U postgres -w postgres -c 'DROP DATABASE accountant;'
psql -h localhost -p 5432 -U postgres -w postgres -c 'CREATE DATABASE accountant;'

psql accountant -h localhost -U postgres < $1