#!/bin/bash
CURRENT_DIR=$(pwd)
APPLICATION_DIR=$HOME/Application
STACK_DIR=$APPLICATION_DIR/stack

source $APPLICATION_DIR/secrets/put_secrets_to_env.sh
docker login -p $REGISTRY_PASSWORD -u $REGISTRY_USER $REGISTRY_URL

cd $STACK_DIR || exit
APPLICATION_YML_VERSION=`date +"%Y-%m-%d_%H-%M-%S"` docker stack deploy --with-registry-auth -c docker-compose.yml application

cd $CURRENT_DIR || exit

source  $APPLICATION_DIR/secrets/clear_secrets_from_env.sh