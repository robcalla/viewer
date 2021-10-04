FROM tomcat:8.5.34-jre8-alpine
RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
ADD ./apps/ROOT.war /usr/local/tomcat/webapps/ROOT.war
RUN mkdir /images
