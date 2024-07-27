SCRIPT_DIR=$(dirname -- $(realpath ${BASH_SOURCE}))
source $SCRIPT_DIR/setup_directories.sh

dos2unix $SECRETS_DIR/*
chmod +x $SECRETS_DIR/*.sh