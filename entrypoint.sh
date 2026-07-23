#!/bin/sh
set -e

PERSISTENCE_FILE=/usr/local/tomcat/webapps/ROOT/WEB-INF/classes/META-INF/persistence.xml

sed -i "s#DB_USER_PLACEHOLDER#${DB_USER}#g" "$PERSISTENCE_FILE"
sed -i "s#DB_PASSWORD_PLACEHOLDER#${DB_PASSWORD}#g" "$PERSISTENCE_FILE"

exec catalina.sh run
