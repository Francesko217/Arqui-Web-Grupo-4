#!/bin/bash
# If SPRING_DATASOURCE_URL is not set, derive it from DATABASE_URL or DB_URL
if [ -z "$SPRING_DATASOURCE_URL" ]; then
  RAW_URL="${DATABASE_URL:-$DB_URL}"
  if [ -n "$RAW_URL" ]; then
    # Strip scheme (postgres:// or postgresql://) and rebuild as jdbc:postgresql://
    export SPRING_DATASOURCE_URL="jdbc:postgresql://${RAW_URL#*//}"
  fi
fi
exec java -jar app.jar
