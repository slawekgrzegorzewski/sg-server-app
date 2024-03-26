#!/bin/bash

mkdir -p $APPLICATION_DIR/fe
mkdir -p $CONFIG_DIR
mkdir -p $DOCKER_DIR/volumes/logs
mkdir -p $DOCKER_DIR/volumes/postgres
mkdir -p $DOCKER_DIR/volumes/database_backup
mkdir -p $MANAGEMENT_DIR/database-backups
mkdir -p $SECRETS_DIR
mkdir -p $STACK_DIR

unzip $APPLICATION_DIR/secrets.zip -d $APPLICATION_DIR/secrets

mv management/* $MANAGEMENT_DIR

dos2unix $APPLICATION_DIR/secrets/*
dos2unix $MANAGEMENT_DIR/*

chmod +x $MANAGEMENT_DIR/*.sh

echo | sudo tee -a ~/.pgpass
sudo chmod 400  ~/.pgpass
