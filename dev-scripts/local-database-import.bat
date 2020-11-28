set PGPASSWORD=SLAwek1!
"C:\Program Files\PostgreSQL\13\bin\psql.exe" -h localhost -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\13\bin\psql.exe" -h localhost -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\13\bin\psql.exe" -h localhost -U postgres -w accountant < "D:\Zrzut produkcji\database-backups\database_2020-11-28_20-00-01.sql"