#!/bin/bash
function die {
    printf '%s\n' "Dying..."
    exit 1
}
token=$(curl -k -s -XPOST --header "X-Auth-Username: user" --header "X-Auth-Password: password" https://localhost:8080/security/public/authenticate) || die
if [[ ${token} == *token* ]]; then
    token=$(echo ${token} | python -c 'import json,sys;obj=json.load(sys.stdin);print obj["token"]')
fi
printf '%s\n' "session_token=${token}"
curl -k -XGET --header "X-Auth-Token: ${token}" https://localhost:8080/resource/activity/1 && printf '\n'

