#!/bin/bash

/home/slag/sg-application/stop.sh

sudo service nginx start
export JWT_SECRET="aofasfIUIOONNgfdsngoi8291519689"
export SENDGRID_API_KEY="afdsjlfkdsjf"
export spring_profiles_active=https


java -jar /home/slag/sg-application/backend/accountant.jar --spring.config.location=file:///home/slag/sg-application/backend/application.yml \
                        1>/home/slag/sg-application/backend/logs/log.txt 2>/home/slag/sg-application/backend/logs/error.txt &

