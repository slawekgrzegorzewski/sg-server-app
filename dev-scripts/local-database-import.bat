cd "D:\Zrzut produkcji\database-backups\current"
D:
for /f %%i in ('dir /b/a-d/od/t:c') do set LAST=%%i
echo The most recently created file is %LAST%

set PGPASSWORD=SLAwek1!
"C:\Program Files\PostgreSQL\13\bin\psql.exe" -h localhost -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\13\bin\psql.exe" -h localhost -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\13\bin\psql.exe" -h localhost -U postgres -w accountant < "D:\Zrzut produkcji\database-backups\current\%LAST%"