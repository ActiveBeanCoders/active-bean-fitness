#!/bin/bash
pkill -f spring-boot \
&& JAVA_HOME=${JAVA8_HOME} \
&& mvn spring-boot:run

