chmod 400  ~/.pgpass

dos2unix *

mkdir deploy
mkdir management

DEPLOY_DIR=../deploy
MANAGEMENT_DIR=../management

cp application.yml.tmpl $DEPLOY_DIR
chmod 644 $DEPLOY_DIR/application.yml.tmpl
cp docker-compose* $DEPLOY_DIR
chmod 644 $DEPLOY_DIR/docker-compose*
cp start*.sh $DEPLOY_DIR
cp stop.sh $DEPLOY_DIR

chmod 755 $DEPLOY_DIR/*.sh

cp backup_data.sh $MANAGEMENT_DIR/
cp restore_data.sh $MANAGEMENT_DIR/
cp setup_machine.sh $MANAGEMENT_DIR/
mkdir $MANAGEMENT_DIR/database-backups
chmod 755 $MANAGEMENT_DIR/*.sh

cd ../
rm -rf tmp
rm docker.zip