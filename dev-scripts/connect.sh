#!/bin/bash
if [ "$(uname)" = "Linux" ]; then
  ssh -i /mnt/d/Development/ovh_private_key slawek@grzegorzewski.org
else
  ssh -i ~/Development/Slawek/ovh_private_key slawek@grzegorzewski.org
fi
