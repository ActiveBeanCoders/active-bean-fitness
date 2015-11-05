#!/bin/bash
function die {
    printf '%s\n' "Dying..."
    exit 1
}

cd common || die
mvn clean install || die
cd ..

cd fitness-parent || die
mvn clean install || die
cd ..

cd resource-api || die
mvn clean install || die
cd ..

cd elasticsearch-api || die
mvn clean install || die
cd ..

cd hibernate-api || die
mvn clean install || die
cd ..

cd security-api || die
mvn clean install || die
cd ..

