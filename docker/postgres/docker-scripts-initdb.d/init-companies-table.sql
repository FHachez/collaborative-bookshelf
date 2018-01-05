CREATE TABLE companies (
    id SERIAL NOT NULL,
    name TEXT NOT NULL,
    PRIMARY KEY( id )
);

COPY companies (name)
FROM '/docker-entrypoint-initdb.d/seeds/companies.csv' WITH CSV DELIMITER ',' QUOTE '"' HEADER;
