#!/bin/bash
function die {
    cat ../scripts/most-recent-maven-build.log
    printf '%s\n' "Dying...See scripts/most-recent-maven-build.log for more info.  Also, make sure you are in the git project's root directory (the folder with the .git folder)."
    exit 1
}

sh scripts/build-apis.sh

services=(
    "data-access-service"
    "elasticsearch-service"
    "gateway"
    "hibernate-service"
    "security-service"
    "ui-angular"
)
printf '%s\n' "Building Services (${apis[*]})"

> scripts/most-recent-maven-build.log || die
if [ ! -e jars ]; then
    mkdir jars
fi
for service in ${services[@]}; do
    cd ${service} || die
    printf '%s\n' "Packaging ${service} SERVICE"
    mvn clean package > ../scripts/most-recent-maven-build.log || die
    cd ..
    jar=$(ls -t ${service}/target/*.jar |head -1)
    cp "${jar}" jars/"${service}.jar"
done

