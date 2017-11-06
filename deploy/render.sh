#!/usr/bin/env bash
for ((i=2; i <=52; i++))
    do
        echo "Running curl request for item $i"
        until $(curl --output /dev/null --silent --head --fail --request POST http://localhost:8080/worker/submitrendertoqueue?itemId=$i&renderType=ITEM);
        do
            printf '.'
            sleep 5
        done
    done