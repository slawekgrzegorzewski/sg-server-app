1. Create a new deployment
   1. windows

      `cmd /C "set DEVELOPMENT_DIRECTORY=D:\OneDrive\Dokumenty\SG app backup&& docker compose up backup-app"`

2. Run and see logs
   1. windows

      `docker start backup-app`

      `docker logs backup-app`