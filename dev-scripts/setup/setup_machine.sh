#!/bin/bash

ADDRESS=$1
REMOTE_USER=$2
USERNAME=$3
COMMAND=$4
HOME_DIR=/home/$USERNAME
shift
shift
shift
shift

function setup {
  echo "params:"
  echo "\$1 - path to docker-ovh.zip and docker-ovh.zip, private_key and cloud_watch_config.json files"
  echo "\$2 - aws_access_key_id"
  echo "\$3 - aws_secret_access_key"
  echo "            of main AWS account"
  echo "\$4 - aws_access_key_id"
  echo "\$5 - aws_secret_access_key"
  echo "            of account in AWS having logs:CreateLogStream and logs:PutLogEvents permissions"
  echo "\$6 - postgres password"

  echo "Login on machine and setup user:"
  echo "sudo adduser $USERNAME; and press ENTER"
  read -p ""

  ssh $REMOTE_USER@$ADDRESS "sudo echo '$USERNAME    ALL=(ALL:ALL) NOPASSWD:ALL' | sudo tee /etc/sudoers.d/$USERNAME"
  scp $1/docker-ovh.zip $1/cloud_watch_config.json $(dirname "$0")/setup_machine_remote.sh $USERNAME@$ADDRESS:$HOME_DIR
  ssh $USERNAME@$ADDRESS "sudo apt-get -y install dos2unix"
  ssh $USERNAME@$ADDRESS "dos2unix /home/$USERNAME/setup_machine_remote.sh"
  ssh $USERNAME@$ADDRESS "/home/$USERNAME/setup_machine_remote.sh $USERNAME '$2' '$3' '$4' '$5' '$6' '$7'"
  ssh $REMOTE_USER@$ADDRESS "sudo echo '$USERNAME    ALL=(ALL:ALL) ALL' | sudo tee /etc/sudoers.d/$USERNAME"

}

function restore_db {
  scp $1/* $USERNAME@$ADDRESS:$HOME_DIR/backup.gz
  ssh $USERNAME@$ADDRESS "gzip -d $HOME_DIR/backup.gz"
  ssh $USERNAME@$ADDRESS "$HOME_DIR/management/restore_data.sh $HOME_DIR/backup"
  ssh $USERNAME@$ADDRESS "rm $HOME_DIR/backup"
}

case $COMMAND in
'setup')
  setup "$@"
  ;;
'restore_db')
  restore_db "$@"
  ;;
esac
