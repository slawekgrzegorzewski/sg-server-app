#!/bin/bash

USERNAME=$1
source ./setup_directories.sh

dos2unix ./*
chmod +x ./*.sh

source ./setup_files.sh
source ./setup_docker.sh

sudo apt-get install -y unzip dos2unix ca-certificates curl gnupg lsb-release postgresql-client-15

echo "*:*:*:postgres:$6" | sudo tee $HOME/.pgpass
sudo chmod 400 $HOME/.pgpass

echo "0 * * * * $DB_MANAGEMENT_DIR/backup_data.sh" | sudo tee -a /var/spool/cron/crontabs/$USERNAME
sudo chown slawek:crontab /var/spool/cron/crontabs/$USERNAME
sudo chmod 600 /var/spool/cron/crontabs/$USERNAME

curl "https://awscli.amazonaws.com/awscli-exe-linux-aarch64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
rm -rf aws awscliv2.zip

mkdir -p $HOME/.aws
echo "[default]" > $HOME/.aws/credentials
echo "aws_access_key_id = $2" >>$HOME/.aws/credentials
echo "aws_secret_access_key = $3" >>$HOME/.aws/credentials
echo "[AmazonCloudWatchAgent]" >>$HOME/.aws/credentials
echo "aws_access_key_id = $4" >>$HOME/.aws/credentials
echo "aws_secret_access_key = $5" >>$HOME/.aws/credentials
chmod 600 $HOME/.aws/credentials

echo "[default]" >$HOME/.aws/config
echo "region = eu-central-1" >>$HOME/.aws/config
echo "output = json" >>$HOME/.aws/config
echo "[profile AmazonCloudWatchAgent]" >>$HOME/.aws/config
echo "region = eu-central-1" >>$HOME/.aws/config
echo "output = json" >> $HOME/.aws/config
chmod 600 $HOME/.aws/config

sudo mkdir -p /etc/systemd/system/docker.service.d/
sudo touch /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "[Service]" | sudo tee /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_ACCESS_KEY_ID=$4\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_SECRET_ACCESS_KEY=$5\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf

sudo systemctl daemon-reload
sudo service docker restart

wget https://amazoncloudwatch-agent.s3.amazonaws.com/ubuntu/arm64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i -E ./amazon-cloudwatch-agent.deb
rm amazon-cloudwatch-agent.deb

sudo cp ./cloud_watch_config.json /opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo dos2unix /opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo chmod 755 /opt/aws/amazon-cloudwatch-agent/bin/config.json

sudo echo "[credentials]" | sudo tee /opt/aws/amazon-cloudwatch-agent/etc/common-config.toml
sudo echo "        shared_credential_file = \"$HOME/.aws/credentials\"" | sudo tee -a /opt/aws/amazon-cloudwatch-agent/etc/common-config.toml

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m onPremise -s -c file:/opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo service amazon-cloudwatch-agent start