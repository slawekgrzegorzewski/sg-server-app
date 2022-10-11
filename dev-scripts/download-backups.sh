#!/bin/bash
if [ "$(uname)" = "Linux" ]; then
  scp -i /mnt/d/Development/ovh_private_key slawek@grzegorzewski.org:/home/slawek/management/database-backups/* "/mnt/d/Development/Zrzut produkcji/database-backups"
  ssh -i /mnt/d/Development/ovh_private_key slawek@grzegorzewski.org rm -rf /home/slawek/management/database-backups/*
else
  scp -i /mnt/:wq/Development/ovh_private_key slawek@grzegorzewski.org:/home/slawek/management/database-backups/* "D:\Development\Zrzut produkcji\database-backups"
  ssh -i /mnt/D/Development/ovh_private_key slawek@grzegorzewski.org rm -rf /home/slawek/management/database-backups/*
fi
