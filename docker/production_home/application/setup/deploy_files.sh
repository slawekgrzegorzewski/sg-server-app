./setup-directories.sh

chmod 400  ~/.pgpass

mv $CONFIG_DIR/logback.xml $APPLICATION_DIR/secrets

NEW_CONFIG_DIR=../config
NEW_STACK_DIR=../stack

cp -f $NEW_STACK_DIR/* $STACK_DIR
cp -f $NEW_CONFIG_DIR/logback.xml $CONFIG_DIR
cp -f $NEW_CONFIG_DIR/nginx.conf $CONFIG_DIR

dos2unix $STACK_DIR/*
dos2unix $CONFIG_DIR/*

chmod 644 $STACK_DIR/*

cd ../../
rm -rf tmp
rm dockerRpi4.zip