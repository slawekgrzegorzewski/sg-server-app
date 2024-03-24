APPLICATION_DIR=$HOME/Application
CONFIG_DIR=$APPLICATION_DIR/config
DOCKER_DIR=$APPLICATION_DIR/docker
MANAGEMENT_DIR=$APPLICATION_DIR/management
STACK_DIR=$APPLICATION_DIR/stack

mkdir -p $DOCKER_DIR/volumes/logs
mkdir -p $DOCKER_DIR/volumes/postgres
mkdir -p $DOCKER_DIR/volumes/database_backup
mkdir -p $MANAGEMENT_DIR/database-backups

unzip $APPLICATION_DIR/secrets.zip -d $APPLICATION_DIR/secrets
mv $CONFIG_DIR/logback.xml $APPLICATION_DIR/secrets

dos2unix $APPLICATION_DIR/secrets/*
dos2unix $CONFIG_DIR/*
dos2unix $MANAGEMENT_DIR/*
dos2unix $STACK_DIR/*

chmod +x $APPLICATION_DIR/secrets/*.sh
chmod +x $MANAGEMENT_DIR/*.sh
chmod 644 $STACK_DIR/application.yml.tmpl
chmod 644 $STACK_DIR/docker-compose*

echo | sudo tee -a ~/.pgpass
sudo chmod 400  ~/.pgpass
