cd "E:\OneDrive\Dokumenty\SG app backup\DB\current"
E:
for /f %%i in ('dir /b/a-d/od/t:c') do set LAST=%%i
echo The most recently created file is %LAST%

set PGPASSWORD=SLAwek1!

"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h 192.168.52.98 -p 5432 -w -U postgres -c "DROP DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h 192.168.52.98 -p 5432 -w -U postgres -c "CREATE DATABASE accountant;" postgres
"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h 192.168.52.98 -U postgres -w accountant < "E:\OneDrive\Dokumenty\SG app backup\DB\current\%LAST%"