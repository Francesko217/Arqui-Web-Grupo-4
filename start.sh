#!/bin/bash
if [ -z "$SPRING_DATASOURCE_URL" ]; then
  RAW_URL="${DATABASE_URL:-$DB_URL}"
  if [ -n "$RAW_URL" ]; then
    WITHOUT_SCHEME="${RAW_URL#*//}"
    USERINFO="${WITHOUT_SCHEME%@*}"
    HOST_DB="${WITHOUT_SCHEME#*@}"
    export SPRING_DATASOURCE_USERNAME="${USERINFO%:*}"
    export SPRING_DATASOURCE_PASSWORD="${USERINFO#*:}"
    export SPRING_DATASOURCE_URL="jdbc:postgresql://${HOST_DB}"
  fi
fi
exec java -jar app.jar
