#!/usr/bin/env bash

chmod -R +x ./docker-scripts-initdb.d

scripts=()

for script in "${scripts[@]}"
do
    echo "RUNNING "$script
    psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" "$POSTGRES_DB" -f "/docker-scripts-initdb.d/"$script
done