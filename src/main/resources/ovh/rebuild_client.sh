#!/bin/bash

/home/slag/sg-application/stop.sh

cd /home/slag/sg-application/client

sudo mv nginx.conf /etc/nginx/
sudo mv mime.types /etc/nginx/

npm install
npm run buildProd

sudo rm -rf /usr/share/nginx/html/*
sudo mv dist/accountant-client/* /usr/share/nginx/html/