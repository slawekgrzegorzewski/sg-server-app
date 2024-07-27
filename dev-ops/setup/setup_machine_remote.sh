#!/bin/bash

USERNAME=$1
HOME_DIR=/home/$USERNAME
MAIN_AWS_ACCESS_KEY_ID=$2
MAIN_AWS_SECRET_ACCESS_KEY=$3
LOGS_AWS_ACCESS_KEY_ID=$4
LOGS_AWS_SECRET_ACCESS_KEY=$5
POSTGRES_PASSWORD=$6
SAMBA_USER=SG-APP-STORAGE
SAMBA_PASSWORD=$7

sudo mkdir -p /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" | sudo tee /etc/apt/sources.list.d/pgdg.list
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo apt-get update
sudo apt-get install -y unzip ca-certificates curl gnupg lsb-release postgresql-client-14 docker-ce docker-ce-cli containerd.io docker-compose-plugin dos2unix samba-common smbclient samba-common-bin smbclient  cifs-utils

unzip $HOME_DIR/docker-ovh.zip -d $HOME_DIR/docker_files
mv $HOME_DIR/docker_files/docker\ -\ ovh $HOME_DIR/docker_files/dockerFiles
ARCHIVE_DIR=$HOME_DIR/docker_files/dockerFiles

mkdir $HOME_DIR/.ssh
cat $ARCHIVE_DIR/id.pub | tee $HOME_DIR/.ssh/authorized_keys
sudo service sshd restart

mkdir $HOME_DIR/deploy
cp -r $ARCHIVE_DIR/certs $HOME_DIR/deploy/
cp $ARCHIVE_DIR/nginx.conf $ARCHIVE_DIR/setup_secrets.sh $HOME_DIR/deploy/
dos2unix $HOME_DIR/deploy/setup_secrets.sh
chmod +x $HOME_DIR/deploy/setup_secrets.sh
mkdir -p $HOME_DIR/docker/volumes/logs
mkdir -p $HOME_DIR/docker/volumes/postgres
mkdir -p $HOME_DIR/management/database-backups

rm $HOME_DIR/docker-ovh.zip
rm -rf $HOME_DIR/docker_files/

sudo usermod -aG docker $USERNAME
sudo systemctl enable docker.service
sudo systemctl enable containerd.service
sudo docker swarm init

echo "*:*:*:postgres:$POSTGRES_PASSWORD" | tee $HOME_DIR/.pgpass
chmod 400 $HOME_DIR/.pgpass

echo "0 * * * * /home/slawek/Cluster/Application/management/backup_data.sh" | sudo tee /var/spool/cron/crontabs/slawek
sudo chown slawek:crontab /var/spool/cron/crontabs/slawek
sudo chmod 600 /var/spool/cron/crontabs/slawek

curl "https://awscli.amazonaws.com/awscli-exe-linux-aarch64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
rm -rf aws awscliv2.zip

mkdir -p $HOME_DIR/.aws
echo "[default]" > $HOME_DIR/.aws/credentials
echo "aws_access_key_id = $MAIN_AWS_ACCESS_KEY_ID" >>$HOME_DIR/.aws/credentials
echo "aws_secret_access_key = $MAIN_AWS_SECRET_ACCESS_KEY" >>$HOME_DIR/.aws/credentials
echo "[AmazonCloudWatchAgent]" >>$HOME_DIR/.aws/credentials
echo "aws_access_key_id = $LOGS_AWS_ACCESS_KEY_ID" >>$HOME_DIR/.aws/credentials
echo "aws_secret_access_key = $LOGS_AWS_SECRET_ACCESS_KEY" >>$HOME_DIR/.aws/credentials
chmod 600 $HOME_DIR/.aws/credentials

echo "[default]" >$HOME_DIR/.aws/config
echo "region = eu-central-1" >>$HOME_DIR/.aws/config
echo "output = json" >>$HOME_DIR/.aws/config
echo "[profile AmazonCloudWatchAgent]" >>$HOME_DIR/.aws/config
echo "region = eu-central-1" >>$HOME_DIR/.aws/config
echo "output = json" >> $HOME_DIR/.aws/config
chmod 600 $HOME_DIR/.aws/config

sudo mkdir -p /etc/systemd/system/docker.service.d/
sudo touch /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "[Service]" | sudo tee /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_ACCESS_KEY_ID=$LOGS_AWS_ACCESS_KEY_ID\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf
sudo echo "Environment=\"AWS_SECRET_ACCESS_KEY=$LOGS_AWS_SECRET_ACCESS_KEY``\"" | sudo tee -a /etc/systemd/system/docker.service.d/aws-credentials.conf

sudo systemctl daemon-reload
sudo service docker restart

wget https://s3.cn-north-1.amazonaws.com.cn/amazoncloudwatch-agent/debian/amd64/latest/amazon-cloudwatch-agent.deb
sudo dpkg -i -E ./amazon-cloudwatch-agent.deb

sudo cp $HOME_DIR/cloud_watch_config.json /opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo dos2unix /opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo chmod 755 /opt/aws/amazon-cloudwatch-agent/bin/config.json

sudo echo "[credentials]" | sudo tee /opt/aws/amazon-cloudwatch-agent/etc/common-config.toml
sudo echo "        shared_credential_file = \"$HOME_DIR/.aws/credentials\"" | sudo tee -a /opt/aws/amazon-cloudwatch-agent/etc/common-config.toml

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m onPremise -s -c file:/opt/aws/amazon-cloudwatch-agent/bin/config.json
sudo service amazon-cloudwatch-agent start

rm $HOME_DIR/amazon-cloudwatch-agent.deb
rm $HOME_DIR/cloud_watch_config.json
rm $HOME_DIR/docker-ovh.zip
rm $HOME_DIR/setup_machine_remote.sh
rm -rf $HOME_DIR/docker_files

echo "//192.168.52.1/Cluster $HOME_DIR/Cluster cifs uid=slawek,gid=slawek,username=$SAMBA_USER,password=$SAMBA_PASSWORD,vers=1.0,file_mode=0777,dir_mode=0777 0 0" | sudo tee -a /etc/fstab