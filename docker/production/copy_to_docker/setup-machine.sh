#!/bin/bash

echo "params:"
echo "\$1 - aws_access_key_id"
echo "\$2 - aws_secret_access_key"
echo "            of main AWS account"
echo "\$3 - aws_access_key_id"
echo "\$4 - aws_secret_access_key"
echo "            of account in AWS having logs:CreateLogStream and logs:PutLogEvents permissions"

mkdir -p /home/slawek/.aws
echo "[default]" > /home/slawek/.aws/credentials
echo "aws_access_key_id = $1" >> /home/slawek/.aws/credentials
echo "aws_secret_access_key = $2" >> /home/slawek/.aws/credentials
echo "[AmazonCloudWatchAgent]" >> /home/slawek/.aws/credentials
echo "aws_access_key_id = $3" >> /home/slawek/.aws/credentials
echo "aws_secret_access_key = $4" >> /home/slawek/.aws/credentials

echo "[default]" > /home/slawek/.aws/config
echo "region = eu-central-1" >> /home/slawek/.aws/config
echo "output = json" >> /home/slawek/.aws/config

sudo mkdir -p /etc/systemd/system/docker.service.d/
sudo touch /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "[Service]" | sudo tee /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_ACCESS_KEY_ID=$3\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_SECRET_ACCESS_KEY=$4\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf

wget https://s3.cn-north-1.amazonaws.com.cn/amazoncloudwatch-agent/debian/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i -E ./amazon-cloudwatch-agent.deb

#sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-config-wizard
sudo copy cloud_watch_config.json /opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo dos2unix /opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo chmod 755 /opt/aws/amazon-cloudwatch-agent/bin/config.json

echo "[credentials]" > /opt/aws/amazon-cloudwatch-agent/etc/common-config.toml
echo "        shared_credential_file = \"/home/slawek/.aws/credentials\"" >> /opt/aws/amazon-cloudwatch-agent/etc/common-config.toml

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m onPremise -s -c file:/opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo service amazon-cloudwatch-agent start