#!/bin/bash
mkdir -p /home/slag/Accountant/database-data
chmod 777 /home/slag/Accountant/database-data
sudo docker-compose -f docker-compose.yml up -d