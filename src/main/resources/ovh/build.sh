#!/bin/bash

sudo pkill -f 'ng'
sudo pkill -f 'java'

cd /home/slag/sg-application/client

sudo rm -rf /etc/nginx/nginx.conf
sudo rm -rf /etc/nginx/mime.types

sudo mv nginx.conf /etc/nginx/
sudo mv mime.types /etc/nginx/

npm install
npm run buildProd

sudo rm -rf /usr/share/nginx/html/*
sudo mv dist/accountant-client/* /usr/share/nginx/html/