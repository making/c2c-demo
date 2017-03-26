#!/bin/sh
echo y | fly -t home sp -p simple-eureka-client -c pipeline.yml -l ../../credentials.yml
