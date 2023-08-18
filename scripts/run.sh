#!/usr/bin/env bash

JAR_FILE=$(ls build/libs/*.jar*)

APP_LOG="application.log"
ERROR_LOG="error.log"
DEPLOY_LOG="deploy.log"

TIME_NOW=$(date + %c)

chmod 755 $JAR_FILE

echo "[ $TIME_NOW ] Run java application $JAR_FILE" >>$DEPLOY_LOG
nohup java -jar $JAR_FILE >$APP_LOG 2>$ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "[ $TIME_NOW ] Application running in PID $CURRENT_PID" >>$DEPLOY_LOG
