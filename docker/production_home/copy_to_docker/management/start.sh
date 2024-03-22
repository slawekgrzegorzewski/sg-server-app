#!/bin/bash
CURRENT_DIR=$(pwd)
APPLICATION_DIR=$HOME/Application
SECRETS_DIR=$APPLICATION_DIR/secrets
STACK_DIR=$APPLICATION_DIR/stack

$SECRETS_DIR/setup_secrets.sh
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 215372400964.dkr.ecr.eu-central-1.amazonaws.com

cd $STACK_DIR
APPLICATION_YML_VERSION=`date +"%Y-%m-%d_%H-%M-%S"` docker stack deploy --with-registry-auth -c docker-compose.yml app
cd $CURRENT_DIR