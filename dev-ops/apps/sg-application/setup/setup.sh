SCRIPT_DIR=$(dirname -- $(realpath ${BASH_SOURCE}))
source $SCRIPT_DIR/setup_directories.sh

dos2unix $SECRETS_DIR/*
chmod +x $SECRETS_DIR/*.sh

mkdir -p $HOME/Cluster/sg-application/docker/volumes/logs
mkdir -p $HOME/Cluster/sg-application/bibleFiles
mkdir -p $HOME/Cluster/sg-application/fe