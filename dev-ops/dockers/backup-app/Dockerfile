FROM ubuntu:kinetic

COPY download-backups.sh /app/download-backups.sh

RUN mkdir /development

## Install packages
RUN apt update && apt install -y  \
    git \
    bash \
    curl \
    openssh-client \
    python3 \
    python3-pip \
    python3-cryptography \
    wget \
    curl

## Install AWSCLI
RUN pip install --upgrade pip && \
    pip install --upgrade awscli

CMD [ "/app/download-backups.sh"]