#!/bin/bash

echo "params:"
echo "\$1 - aws_access_key_id"
echo "\$2 - aws_secret_access_key"
echo "            of account in AWS having logs:CreateLogStream and logs:PutLogEvents permissions"

sudo mkdir -p /etc/systemd/system/docker.service.d/
sudo touch /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "[Service]" | sudo tee /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_ACCESS_KEY_ID=$1\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_SECRET_ACCESS_KEY=$2\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf