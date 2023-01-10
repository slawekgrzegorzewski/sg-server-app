#!/bin/bash
if [ "$(uname)" = "Linux" ]; then
  ssh -i /mnt/d/OneDrive/Dokumenty/Development/ovh_private_key slawek@grzegorzewski.org "$@"
else
  ssh -i ~/Development/Slawek/ovh_private_key slawek@grzegorzewski.org "$@"
fi
