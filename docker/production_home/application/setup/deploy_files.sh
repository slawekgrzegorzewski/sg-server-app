chmod 400  ~/.pgpass

SECRETS_DIR=../../secrets
STACK_DIR=../../stack
MANAGEMENT_DIR=../../management

NEW_CONFIG_DIR=../config
NEW_STACK_DIR=../stack
NEW_MANAGEMENT_DIR=../management

cp -f $NEW_STACK_DIR/* $STACK_DIR
cp -f $NEW_MANAGEMENT_DIR/* $MANAGEMENT_DIR
cp -f $NEW_CONFIG_DIR/logback.xml $SECRETS_DIR
cp -f $NEW_CONFIG_DIR/nginx.conf $SECRETS_DIR

dos2unix $STACK_DIR/*
dos2unix $MANAGEMENT_DIR/*
dos2unix $SECRETS_DIR/logback.xml
dos2unix $SECRETS_DIR/nginx.conf

chmod 644 $STACK_DIR/*
chmod 755 $MANAGEMENT_DIR/*.sh

cd ../../
rm -rf tmp
rm dockerRpi4.zip