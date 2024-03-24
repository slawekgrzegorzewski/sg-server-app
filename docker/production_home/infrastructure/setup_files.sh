mkdir -p $APPLICATION_DIR
mkdir -p $CONFIG_DIR
mkdir -p $DOCKER_DIR/volumes/logs
mkdir -p $DOCKER_DIR/volumes/postgres
mkdir -p $DOCKER_DIR/volumes/database_backup
mkdir -p $MANAGEMENT_DIR/database-backups
mkdir -p $SECRETS_DIR
mkdir -p $STACK_DIR

unzip $APPLICATION_DIR/secrets.zip -d $APPLICATION_DIR/secrets

dos2unix $APPLICATION_DIR/secrets/*
dos2unix $MANAGEMENT_DIR/*

chmod +x $APPLICATION_DIR/secrets/*.sh
chmod +x $MANAGEMENT_DIR/*.sh

mv managemenent $APPLICATION_DIR

echo | sudo tee -a ~/.pgpass
sudo chmod 400  ~/.pgpass
