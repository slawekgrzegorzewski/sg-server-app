source ./setup_directories.sh

sudo chmod 400  ~/.pgpass

SOURCE_CONFIG_DIR=../config
SOURCE_STACK_DIR=../
SOURCE_MANAGEMENT_DIR=../management

dos2unix $SOURCE_MANAGEMENT_DIR/*
cp -f $SOURCE_MANAGEMENT_DIR/* $MANAGEMENT_DIR
cp -f $SOURCE_STACK_DIR/docker-compose.yml $STACK_DIR
cp -f $SOURCE_CONFIG_DIR/logback.xml $CONFIG_DIR
cp -f $SOURCE_CONFIG_DIR/nginx.conf $CONFIG_DIR
cp -f $SOURCE_CONFIG_DIR/application.yml.tmpl $CONFIG_DIR

dos2unix $STACK_DIR/*
dos2unix $CONFIG_DIR/*

chmod 644 $STACK_DIR/*

cd ../../
rm -rf tmp
rm dockerRpi4.zip