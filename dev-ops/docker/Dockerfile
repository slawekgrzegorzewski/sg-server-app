FROM node:14-alpine

# Install packages
RUN apk update && apk add --update --no-cache \
    git \
    bash \
    curl \
    openssh \
    python3 \
    py3-pip \
    py-cryptography \
    wget \
    curl

# Install AWSCLI
RUN pip install --upgrade pip && \
    pip install --upgrade awscli

COPY