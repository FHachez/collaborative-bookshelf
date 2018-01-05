CREATE TABLE accounts (
    username TEXT NOT NULL,
    company_id SERIAL NOT NULL,
    password TEXT NOT NULL,
    salt TEXT NOT NULL,
    PRIMARY KEY( username ),
    FOREIGN KEY ( company_id ) REFERENCES companies ( id )
);

COPY accounts (username, company_id, password, salt)
FROM '/docker-entrypoint-initdb.d/seeds/accounts.csv' WITH CSV DELIMITER ',' QUOTE '"' HEADER;
