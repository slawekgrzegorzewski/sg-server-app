#!/bin/bash
if [ ! -d /home/slag/Accountant/database-data ]; then
  echo "Creating directory"
  mkdir -p /home/slag/Accountant/database-data
  chmod 777 /home/slag/Accountant/database-data
fi
chmod 777 /home/slag/Accountant/clean.sh
sudo docker-compose -f docker-compose.yml up -d