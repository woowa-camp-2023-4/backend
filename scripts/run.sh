#!/usr/bin/env bash

PROJECT_ROOT="$HOME/backend"
JAR_FILE=build/libs/$(ls build/libs/*.jar*)

APP_LOG="$PROJECT_ROOT/log/application.log"
ERROR_LOG="$PROJECT_ROOT/log/error.log"
DEPLOY_LOG="$PROJECT_ROOT/log/deploy.log"

TIME_NOW=$(date + %c)

echo "[ $TIME_NOW ] Run java application $JAR_FILE" >>$DEPLOY_LOG
nohup java -jar $JAR_FILE >$APP_LOG 2>$ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "[ $TIME_NOW ] Application running in PID $CURRENT_PID" >>$DEPLOY_LOG
