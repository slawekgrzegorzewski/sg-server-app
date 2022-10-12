#!/bin/bash
if [ "$(uname)" = "Linux" ]; then
  scp -i /mnt/d/Development/ovh_private_key slawek@grzegorzewski.org:/home/slawek/management/database-backups/* "/mnt/d/Development/Zrzut produkcji/database-backups"
  ssh -i /mnt/d/Development/ovh_private_key slawek@grzegorzewski.org rm -rf /home/slawek/management/database-backups/*
else
  scp -i ~/Development/Slawek/ovh_private_key slawek@grzegorzewski.org:/home/slawek/management/database-backups/* ~/Development/Slawek/backups
  ssh -i ~/Development/Slawek/ovh_private_key slawek@grzegorzewski.org ls /home/slawek/management/database-backups/*
fi
