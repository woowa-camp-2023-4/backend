#!/usr/bin/env bash

JAR_FILE=$(ls build/libs/*.jar*)

APP_LOG="application.log"
ERROR_LOG="error.log"
DEPLOY_LOG="deploy.log"

TIME_NOW=$(date)


PREVIOUS_PID=$(pgrep -f woowakit)

if [ -n $PREVIOUS_PID ]
then
  echo "[ $TIME_NOW ] Kill previous process $PREVIOUS_PID" >>$DEPLOY_LOG
  kill -9 $PREVIOUS_PID
fi

chmod 755 $JAR_FILE

echo "[ $TIME_NOW ] Run java application $JAR_FILE" >>$DEPLOY_LOG
nohup java -jar -Dspring.profiles.active=local $JAR_FILE >$APP_LOG 2>$ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "[ $TIME_NOW ] Application running in PID $CURRENT_PID" >>$DEPLOY_LOG
