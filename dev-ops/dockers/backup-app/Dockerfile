FROM ubuntu:kinetic

COPY --chmod=755 download-backups.sh /app/

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