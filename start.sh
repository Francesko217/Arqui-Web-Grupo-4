#!/bin/bash
if [ -n "$DATABASE_URL" ]; then
  export SPRING_DATASOURCE_URL="jdbc:postgresql${DATABASE_URL#postgres}"
elif [ -n "$DB_URL" ]; then
  export SPRING_DATASOURCE_URL="jdbc:postgresql${DB_URL#postgres}"
fi
exec java -jar target/TuTrade-0.0.1-SNAPSHOT.jar
