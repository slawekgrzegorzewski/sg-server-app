#!/bin/bash

SCRIPT_DIR=$(dirname -- $(realpath ${BASH_SOURCE}))
source $SCRIPT_DIR/setup_directories.sh

source $SECRETS_DIR/put_secrets_to_env.sh

$SECRETS_DIR/setup_registry_auth.sh $REGISTRY_USER $REGISTRY_PASSWORD $CORE_CONFIG_DIR

mkdir -p $REGISTRY_DATA_DIR

source $SECRETS_DIR/clear_secrets_from_env.sh

