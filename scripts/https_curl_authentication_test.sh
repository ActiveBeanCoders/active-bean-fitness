#!/bin/bash
function die {
    printf '%s\n' "Dying..."
    exit 1
}

username="dan" # default
password="dan" # default
security_url="https://localhost:9999" # default
api_url="https://localhost:8086/api/activity/2" # default


while getopts ":t:u:p:a:" n; do
    case ${n} in
        t)
            token="${OPTARG}"
            ;;
        a)
            api_url="${OPTARG}"
            ;;
        u)
            username="${OPTARG}"
            ;;
        p)
            password="${OPTARG}"
            ;;
    esac
done

if [[ "${token}" == "" ]]; then
    token=$(curl -k -s -XPOST --header "X-Auth-Username: ${username}" --header "X-Auth-Password: ${password}" ${security_url}/public/authenticate) || die
    if [[ ${token} == *token* ]]; then
        token=$(echo ${token} | python -c 'import json,sys;obj=json.load(sys.stdin);print obj["token"]')
    fi
fi
printf '%s\n' "session_token=${token}"
curl -k -XGET --header "X-Auth-Token: ${token}" ${api_url} && printf '\n'

