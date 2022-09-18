#!/bin/bash

./setup_secrets.sh
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 215372400964.dkr.ecr.eu-central-1.amazonaws.com
docker stack deploy --with-registry-auth -c docker-compose-debug.yml app