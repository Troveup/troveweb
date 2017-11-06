#!/usr/bin/env bash

#Bash script for copying the views directory to the target directory to prevent completely re-building
#the environment

cp -r ../src/main/webapp/WEB-INF/views/* ../target/brooklyn-1.0-SNAPSHOT/WEB-INF/views/
cp -r ../src/main/webapp/WEB-INF/resources/js/* ../target/brooklyn-1.0-SNAPSHOT/WEB-INF/resources/js/

