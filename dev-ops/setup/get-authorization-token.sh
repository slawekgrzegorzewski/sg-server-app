#!/usr/bin/env bash
set -e

aws codeartifact \
  --profile sg-app \
  get-authorization-token \
  --domain sg-repository \
  --domain-owner 215372400964 \
  --region eu-central-1 \
  --query authorizationToken \
  --output text