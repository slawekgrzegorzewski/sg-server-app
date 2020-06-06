#Installation
FROM ubuntu:20.04
RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt install -y gnupg && \
    apt install -y postgresql postgresql-contrib
RUN apt install -y openjdk-14-jdk && java --version

#Postgres
USER postgres
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/12/main/pg_hba.conf && \
    echo "listen_addresses='*'" >> /etc/postgresql/12/main/postgresql.conf && \
    /etc/init.d/postgresql start && \
    psql --command "ALTER USER postgres WITH SUPERUSER PASSWORD 'SLAwek1!';" && \
    createdb -O postgres accountant

#App
EXPOSE 5432

CMD ["/usr/lib/postgresql/12/bin/postgres", "-D", "/var/lib/postgresql/12/main", "-c", "config_file=/etc/postgresql/12/main/postgresql.conf"]