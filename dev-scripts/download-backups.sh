#!/bin/bash
if [ "$(uname)" = "Linux" ]; then
  scp -i /mnt/d/OneDrive/Dokumenty/Development/ovh_private_key slawek@grzegorzewski.org:/home/slawek/management/database-backups/* "/mnt/d/OneDrive/Dokumenty/Development/Zrzut produkcji/database-backups"
  ssh -i /mnt/d/OneDrive/Dokumenty/Development/ovh_private_key slawek@grzegorzewski.org rm -rf /home/slawek/management/database-backups/*
  aws s3 sync s3://intellectualpropertytask /mnt/d/OneDrive/Dokumenty/Development/S3
  rm -rf /mnt/d/OneDrive/Dokumenty/Development/3cpy
  cp -r /mnt/d/OneDrive/Dokumenty/Development/S3 /mnt/d/OneDrive/Dokumenty/Development/S3cpy
else
  scp -i ~/Development/Slawek/ovh_private_key slawek@grzegorzewski.org:/home/slawek/management/database-backups/* ~/Development/Slawek/backups
  ssh -i ~/Development/Slawek/ovh_private_key slawek@grzegorzewski.org ls /home/slawek/management/database-backups/*
  aws s3 sync s3://intellectualpropertytask ~/Development/Slawek/S3 --profile sg-app
  rm -rf ~/Development/Slawek/S3cpy
  cp -r ~/Development/Slawek/S3 ~/Development/Slawek/S3cpy
fi
D:\OneDrive\Dokumenty\Development