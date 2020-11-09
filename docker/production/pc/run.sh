#!/bin/bash
sudo docker-compose -f docker-compose.yml --tlsverify --tlscacert /home/slag/.docker/ca.pem --tlscert /home/slag/.docker/cert.pem --tlskey /home/slag/.docker/key.pem up