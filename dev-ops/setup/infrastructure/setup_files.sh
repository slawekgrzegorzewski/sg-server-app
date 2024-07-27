#!/bin/bash

mkdir -p $SG_APPLICATION_DIR/fe
mkdir -p $APPLICATION_CONFIG_DIR
mkdir -p $DB_MANAGEMENT_DIR/database-backups
mkdir -p $SECRETS_DIR
mkdir -p $APPLICATION_STACK_DIR

unzip $SG_APPLICATION_DIR/secrets.zip -d $SG_APPLICATION_DIR/secrets

mv management/* $MANAGEMENT_DIR

dos2unix $SG_APPLICATION_DIR/secrets/*
dos2unix $MANAGEMENT_DIR/*

chmod +x $MANAGEMENT_DIR/*.sh

echo | sudo tee -a ~/.pgpass
sudo chmod 400  ~/.pgpass
