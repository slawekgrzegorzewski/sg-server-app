#!/bin/bash

chmod 777 /home/slag/sg-application/stop.sh
chmod 777 /home/slag/sg-application/start.sh
chmod 777 /home/slag/sg-application/backup_data.sh
chmod 777 /home/slag/sg-application/restore_data.sh

/home/slag/sg-application/stop.sh

cd /home/slag/sg-application

mkdir -p backend/logs
mkdir -p client/logs

mkdir database-backups
mv .pgpass /home/slag/
chmod 0600 /home/slag/.pgpass

cd /home/slag/sg-application/client

sudo mv nginx.conf /etc/nginx/
sudo mv mime.types /etc/nginx/

npm install
npm run buildProd

sudo rm -rf /usr/share/nginx/html/*
sudo mv dist/accountant-client/* /usr/share/nginx/html/

echo "IMPORTANT!!! Remember to set postgres password in /home/slag/.pgpass"
echo "IMPORTANT!!! Remember to set passwords in /home/slag/sg-application/backend/application.yml"