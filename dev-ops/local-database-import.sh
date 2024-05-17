#!/bin/bash

BACKUPS_DIR=/Users/slawekgrzegorzewski/Library/CloudStorage/OneDrive-Osobisty/Dokumenty/SG\ app\ backup/DB/current
FILE=$(ls "$BACKUPS_DIR")
BACKUP="$BACKUPS_DIR/$FILE"
echo "$BACKUP"

psql postgres://postgres:SLAwek1!@localhost:15432/postgres -c "DROP DATABASE accountant;"
psql postgres://postgres:SLAwek1!@localhost:15432/postgres -c "CREATE DATABASE accountant;"
psql postgres://postgres:SLAwek1!@localhost:15432/accountant < "$BACKUP"