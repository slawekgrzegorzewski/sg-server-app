#!/bin/bash

export POSTGRES_DATA_DIR=$HOME/Docker/data

export CLUSTER_DIR=$HOME/Cluster
export SECRETS_DIR=$CLUSTER_DIR/secrets
export PERMANENT_DATA_DIR=$CLUSTER_DIR/permanent_data

export CORE_DIR=$CLUSTER_DIR/core
export CORE_MANAGEMENT_DIR=$CORE_DIR/management
export CORE_SETUP_DIR=$CORE_DIR/setup
export CORE_STACK_DIR=$CORE_DIR/stack
export CORE_CONFIG_DIR=$CORE_STACK_DIR/config
export REGISTRY_DIR=$CORE_STACK_DIR/registry
export REGISTRY_DATA_DIR=$REGISTRY_DIR/data

export SG_APPLICATION_DIR=$CLUSTER_DIR/sg-application
export DOCKER_DIR=$SG_APPLICATION_DIR/docker
export SG_APPLICATION_APPLICATION_DIR=$SG_APPLICATION_DIR/management
export APPLICATION_STACK_DIR=$SG_APPLICATION_DIR/stack
export APPLICATION_CONFIG_DIR=$APPLICATION_STACK_DIR/config