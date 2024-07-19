source ./setup_directories.sh

sudo chmod 400  ~/.pgpass

SOURCE_CONFIG_DIR=../config
SOURCE_STACK_DIR=..
SOURCE_MANAGEMENT_DIR=../management

sudo dos2unix $SOURCE_CONFIG_DIR/*
sudo dos2unix $SOURCE_STACK_DIR/*
sudo dos2unix $SOURCE_MANAGEMENT_DIR/*
sudo chmod 644 $SOURCE_STACK_DIR/*
sudo cp -f $SOURCE_MANAGEMENT_DIR/* $MANAGEMENT_DIR
sudo cp -f $SOURCE_STACK_DIR/docker-compose.yml $STACK_DIR
sudo cp -f $SOURCE_CONFIG_DIR/logback.xml $CONFIG_DIR
sudo cp -f $SOURCE_CONFIG_DIR/nginx.conf $CONFIG_DIR
sudo cp -f $SOURCE_CONFIG_DIR/application.yml.tmpl $CONFIG_DIR