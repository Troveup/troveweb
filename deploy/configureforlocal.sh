#!/usr/bin/env bash

#Check which option the user wants
echo Please type Dev, Usertest, QA, Clone, or Production to change your config to the desired environment:
read push_type

#Lowercase this thing so that all inputs work
lowered_push_type=$(echo $push_type | awk '{print tolower($0)}')

#Back up the config files
cp ../src/main/java/com/troveup/config/properties/application.properties ../src/main/java/com/troveup/config/properties/application.orig
cp ../src/main/webapp/WEB-INF/appengine-web.xml ../src/main/webapp/WEB-INF/appengine-web.orig

#Execute the push for whatever environment we're trying to push to
if [ $lowered_push_type = "dev" ]
then
    cp ../src/main/java/com/troveup/config/properties/application-dev.switchme ../src/main/java/com/troveup/config/properties/application.properties
    cp ../src/main/webapp/WEB-INF/appengine-web-dev.switchme ../src/main/webapp/WEB-INF/appengine-web.xml
elif [ $lowered_push_type = "qa" ]
then
    cp ../src/main/java/com/troveup/config/properties/application-qa.switchme ../src/main/java/com/troveup/config/properties/application.properties
    cp ../src/main/webapp/WEB-INF/appengine-web-qa.switchme ../src/main/webapp/WEB-INF/appengine-web.xml
elif [ $lowered_push_type = "production" ]
then
    cp ../src/main/java/com/troveup/config/properties/application-prod.switchme ../src/main/java/com/troveup/config/properties/application.properties
    cp ../src/main/webapp/WEB-INF/appengine-web-prod.switchme ../src/main/webapp/WEB-INF/appengine-web.xml
elif [ $lowered_push_type = "usertest" ]
then
    cp ../src/main/java/com/troveup/config/properties/application-usertest.switchme ../src/main/java/com/troveup/config/properties/application.properties
    cp ../src/main/webapp/WEB-INF/appengine-web-usertest.switchme ../src/main/webapp/WEB-INF/appengine-web.xml
elif [ $lowered_push_type = "clone" ]
then
    cp ../src/main/java/com/troveup/config/properties/application-clone.switchme ../src/main/java/com/troveup/config/properties/application.properties
    cp ../src/main/webapp/WEB-INF/appengine-web-clone.switchme ../src/main/webapp/WEB-INF/appengine-web.xml
else
    echo Invalid input or not yet implemented!
fi