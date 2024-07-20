source ./setup_directories.sh

CURRENT_DIR=$(pwd)
TMP_DIR=$(realpath ..)
SOURCE_STACK_DIR=$TMP_DIR
SOURCE_CONFIG_DIR=$TMP_DIR/config
SOURCE_MANAGEMENT_DIR=$TMP_DIR/management

cd $SOURCE_CONFIG_DIR
sudo dos2unix application.yml.tmpl  logback.xml  nginx.conf

cd $SOURCE_STACK_DIR
sudo dos2unix docker-compose.yml
sudo chmod 644 docker-compose.yml

cd $SOURCE_MANAGEMENT_DIR
sudo dos2unix *.sh

cd $CURRENT_DIR

sudo cp -f $SOURCE_MANAGEMENT_DIR/*.sh $MANAGEMENT_DIR
sudo cp -f $SOURCE_STACK_DIR/docker-compose.yml $STACK_DIR
sudo cp -f $SOURCE_CONFIG_DIR/logback.xml $CONFIG_DIR
sudo cp -f $SOURCE_CONFIG_DIR/nginx.conf $CONFIG_DIR
sudo cp -f $SOURCE_CONFIG_DIR/application.yml.tmpl $CONFIG_DIR