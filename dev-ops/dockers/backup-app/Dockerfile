FROM ubuntu:lunar

COPY --chmod=755 download-backups.sh /app/backup.sh

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
    awscli \
    wget \
    curl \
    dos2unix

RUN dos2unix /app/backup.sh

CMD ["/app/backup.sh"]