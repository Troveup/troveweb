#!/usr/bin/env bash

#Check which option the user wants
echo Please type Dev, QA, Clone, or Production to push to your desired environment:
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
else
    echo Invalid input or not yet implemented!
fi

#Clean, install, and perform a push
cd ../
mvn clean install appengine:devserver
cd deploy

#Restore the original configs
#a git checkin
cp ../src/main/java/com/troveup/config/properties/application.orig ../src/main/java/com/troveup/config/properties/application.properties
cp ../src/main/webapp/WEB-INF/appengine-web.orig ../src/main/webapp/WEB-INF/appengine-web.xml
rm ../src/main/java/com/troveup/config/properties/application.orig
rm ../src/main/webapp/WEB-INF/appengine-web.orig