#!/bin/bash
token=$(curl -s -XPOST --header "X-Auth-Username: user" --header "X-Auth-Password: password" http://localhost:8080/security/public/authenticate) && printf '%s\n' "session_token=${token}" && curl -XGET --header "X-Auth-Token: ${token}" http://localhost:8080/resource/activity/1 && printf '\n'

