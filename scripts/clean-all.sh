#!/bin/bash
poms=$(find . -name "pom.xml")
for pom in ${poms[@]}; do
    folder=${pom%/*}
    moved=false
    cd "${folder}" && moved=true && mvn clean
    if [[ ${moved} == true ]]; then
        cd ..
    fi
done
