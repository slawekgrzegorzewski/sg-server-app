#Installation
FROM ubuntu:20.04
RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt install -y gnupg && \
    apt install -y postgresql postgresql-contrib

#App
EXPOSE 8081
USER root
RUN mkdir -p /home/accountant/log
WORKDIR /home/accountant

RUN echo "#!/bin/bash" > /home/accountant/start.sh && \
    echo "/etc/init.d/postgresql start" >> /home/accountant/start.sh && \
    echo "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar /home/accountant/accountant.jar | tee /home/accountant/log/accountant.log" >> /home/accountant/start.sh && \
    cat /home/accountant/start.sh && \
    chmod +x /home/accountant/start.sh

COPY application.yml /home/accountant/application.yml
COPY accountant.jar /home/accountant/accountant.jar
CMD /bin/bash /home/accountant/start.sh