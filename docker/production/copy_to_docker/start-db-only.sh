#!/bin/bash

./setup_secrets.sh
docker stack deploy -c docker-compose-db-only.yml app