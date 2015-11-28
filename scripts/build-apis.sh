#!/bin/bash
function die {
    cat ../scripts/most-recent-maven-build.log
    printf '%s\n' "Dying...See scripts/most-recent-maven-build.log for more info."
    exit 1
}

cd common || die
printf '%s\n' "Installing common"
mvn clean install > ../scripts/most-recent-maven-build.log || die
cd ..

cd security-api || die
printf '%s\n' "Installing security-api"
mvn clean install > ../scripts/most-recent-maven-build.log || die
cd ..

cd fitness-parent || die
printf '%s\n' "Installing fitness-parent"
mvn clean install > ../scripts/most-recent-maven-build.log || die
cd ..

cd data-access-api || die
printf '%s\n' "Installing data-access-api"
mvn clean install > ../scripts/most-recent-maven-build.log || die
cd ..

cd elasticsearch-api || die
printf '%s\n' "Installing elasticsearch-api"
mvn clean install > ../scripts/most-recent-maven-build.log || die
cd ..

cd hibernate-api || die
printf '%s\n' "Installing hibernate-api"
mvn clean install > ../scripts/most-recent-maven-build.log || die
cd ..

