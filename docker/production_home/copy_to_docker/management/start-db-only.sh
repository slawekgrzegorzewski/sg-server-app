#!/bin/bash
CURRENT_DIR=$(pwd)
APPLICATION_DIR=$HOME/Application
SECRETS_DIR=$APPLICATION_DIR/secrets
STACK_DIR=$APPLICATION_DIR/stack

$SECRETS_DIR/setup_secrets.sh

cd $STACK_DIR
docker stack deploy -c docker-compose-db-only.yml app
cd $CURRENT_DIR