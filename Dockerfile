FROM tomcat:10.1-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*
RUN mkdir -p /usr/local/tomcat/webapps/ROOT

COPY target/gestion-cotisations.war /tmp/ROOT.war
RUN cd /usr/local/tomcat/webapps/ROOT && jar xf /tmp/ROOT.war && rm /tmp/ROOT.war

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]