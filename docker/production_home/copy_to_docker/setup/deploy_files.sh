chmod 400  ~/.pgpass

STACK_DIR=../../stack
MANAGEMENT_DIR=../../management

NEW_STACK_DIR=../stack
NEW_MANAGEMENT_DIR=../management

cp -f $NEW_STACK_DIR=/* $STACK_DIR
cp -f $NEW_MANAGEMENT_DIR/* $MANAGEMENT_DIR

dos2unix $STACK_DIR/*
dos2unix $MANAGEMENT_DIR/*

chmod 644 $STACK_DIR/*
chmod 755 $MANAGEMENT_DIR/*.sh

cd ../../
rm -rf tmp
rm dockerRpi4.zip