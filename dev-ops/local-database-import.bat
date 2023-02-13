cd "D:\OneDrive\Dokumenty\Development\Zrzut produkcji\database-backups\current"
D:
for /f %%i in ('dir /b/a-d/od/t:c') do set LAST=%%i
echo The most recently created file is %LAST%

set PGPASSWORD=SLAwek1!
rem "C:\Program Files\PostgreSQL\14\bin\psql.exe" -h localhost -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
rem "C:\Program Files\PostgreSQL\14\bin\psql.exe" -h localhost -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
rem "C:\Program Files\PostgreSQL\14\bin\psql.exe" -h localhost -U postgres -w accountant < "D:\OneDrive\Dokumenty\Development\Zrzut produkcji\database-backups\current\%LAST%"

"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h 192.168.52.98 -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h 192.168.52.98 -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h 192.168.52.98 -U postgres -w accountant < "D:\OneDrive\Dokumenty\Development\Zrzut produkcji\database-backups\current\%LAST%"