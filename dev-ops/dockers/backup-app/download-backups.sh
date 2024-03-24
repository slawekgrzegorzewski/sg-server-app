#!/bin/bash
scp -i /development/ovh_private_key slawek@grzegorzewski.org:/home/slawek/Application/management/database-backups/* "/development/DB/"
ssh -i /development/ovh_private_key slawek@grzegorzewski.org rm -rf /home/slawek/Application/management/database-backups/*
aws s3 sync --delete s3://intellectualpropertytask /development/S3
rm -rf /development/S3cpy
cp -r /development/S3 /development/S3cpy