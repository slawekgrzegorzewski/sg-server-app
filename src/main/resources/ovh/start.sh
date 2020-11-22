#!/bin/bash

sudo pkill -f 'java'

export JWT_TOKEN="aofasfIUIOONNgfdsngoi8291519689"
export spring_profiles_active=https


java -jar /home/slag/sg-application/backend/accountant.jar --spring.config.location=file:///home/slag/sg-application/backend/application.yml \
                        1>/home/slag/sg-application/backend/logs/log.txt 2>/home/slag/sg-application/backend/logs/error.txt &

