#!/bin/bash

DB_DIR=/development/DB

sshpass -f /development/ssh_pass scp slawek@grzegorzewski.org:/home/slawek/Application/management/database-backups/* "$DB_DIR"
sshpass -f /development/ssh_pass ssh slawek@grzegorzewski.org rm -rf /home/slawek/Application/management/database-backups/*
aws s3 sync --delete s3://intellectualpropertytask /development/S3
rm -rf /development/S3cpy
cp -r /development/S3 /development/S3cpy

FILE_NAME=$(ls "$DB_DIR" | sort | tail -n 1)
echo "FILE_NAME="$FILE_NAME
rm "$DB_DIR"/current/*
cp  "$DB_DIR"/$FILE_NAME "$DB_DIR"/current
gzip -d "$DB_DIR"/current/$FILE_NAME