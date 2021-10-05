FROM tomcat:9.0.20-jre8-alpine
ENV CATALINA_OPTS -Dsecurerandom.source=file:/dev/urandom
COPY ./target/ROOT.war /usr/local/tomcat/webapps/ROOT.war
COPY /libraries /usr/local/tomcat/lib
RUN mkdir /images; chmod 777 /images;rm -fr /usr/local/tomcat/webapps/ROOT/*; chmod -R a+rwX /usr/local/tomcat; cd /usr/local/tomcat/webapps/ROOT; unzip ../ROOT.war;chmod -R a+rwX /usr/local/tomcat; rm ../ROOT.war
