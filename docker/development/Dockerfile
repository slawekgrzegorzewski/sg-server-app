#Installation
FROM ubuntu:19.10
RUN apt-get update && apt-get install -y gnupg
RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8
RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list && \
    apt-get update && apt-get install -y postgresql-11 postgresql-client-11 postgresql-contrib-11
RUN apt-get install -y openjdk-11-jdk && java --version

#Postgres
USER postgres
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/11/main/pg_hba.conf
RUN echo "listen_addresses='*'" >> /etc/postgresql/11/main/postgresql.conf
RUN  /etc/init.d/postgresql start &&\
    psql --command "ALTER USER postgres WITH SUPERUSER PASSWORD 'SLAwek1!';" &&\
    createdb -O postgres accountant

#App
EXPOSE 5432
EXPOSE 8080
EXPOSE 5005
USER root
RUN mkdir -p /home/accountant/log
WORKDIR /home/accountant

RUN echo "#!/bin/bash" > /home/accountant/start.sh
RUN echo "/etc/init.d/postgresql start" >> /home/accountant/start.sh
RUN echo "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar /home/accountant/accountant.jar | tee /home/accountant/log/accountant.log" >> /home/accountant/start.sh
RUN cat /home/accountant/start.sh
RUN chmod +x /home/accountant/start.sh

COPY application.yml /home/accountant/application.yml
COPY accountant.jar /home/accountant/accountant.jar
CMD /bin/bash /home/accountant/start.sh