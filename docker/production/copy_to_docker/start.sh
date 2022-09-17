#!/bin/bash

./setup_secrets.sh
docker stack deploy -c docker-compose.yml app