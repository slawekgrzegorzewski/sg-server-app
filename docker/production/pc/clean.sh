#!/bin/bash
sudo docker-compose -f docker-compose.yml down
sudo docker system prune -a -f
rm -rf backend client database docker-compose.yml run.sh clean.sh