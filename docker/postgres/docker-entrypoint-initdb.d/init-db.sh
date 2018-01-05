#!/usr/bin/env bash

chmod -R +x ./docker-scripts-initdb.d

scripts=( 
    "init-companies-table.sql"  
    "init-accounts-table.sql" 
    "init-clients-table.sql"
)

for script in "${scripts[@]}"
do
    echo "RUNNING "$script
    psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" "$POSTGRES_DB" -f "/docker-scripts-initdb.d/"$script
done