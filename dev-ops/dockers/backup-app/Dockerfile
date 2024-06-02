FROM ubuntu:mantic

RUN mkdir /development

## Install packages
RUN TZ=Europe/Warsaw && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    apt update && \
    apt install -y git bash curl openssh-client sshpass \
                    python3 python3-pip python3-cryptography \
                    awscli wget curl dos2unix \
                    gzip

COPY --chmod=755 download-backups.sh /app/backup.sh

RUN dos2unix /app/backup.sh

CMD ["/app/backup.sh"]