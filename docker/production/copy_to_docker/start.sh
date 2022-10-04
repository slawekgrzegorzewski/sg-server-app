#!/bin/bash

./setup_secrets.sh
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 215372400964.dkr.ecr.eu-central-1.amazonaws.com
APPLICATION_YML_VERSION=`date +"%Y-%m-%d_%H-%M-%S"` docker stack deploy --with-registry-auth -c docker-compose.yml app