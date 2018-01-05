CREATE TABLE clients (
    id SERIAL NOT NULL,
    company_id SERIAL NOT NULL,
    first_name TEXT NOT NULL,
    middle_name TEXT,
    last_name TEXT NOT NULL,
    gender TEXT NOT NULL,
    date_of_birth date NOT NULL,
    place_of_birth TEXT NOT NULL,
    national_number TEXT NOT NULL,
    nationality TEXT NOT NULL,
    municipality TEXT NOT NULL,
    zip_code TEXT NOT NULL,
    street_name TEXT NOT NULL,
    number TEXT NOT NULL,
    PRIMARY KEY( id ),
    FOREIGN KEY ( company_id ) REFERENCES companies ( id )
);


CREATE INDEX clients_cid_idx ON clients(company_id);


COPY clients (company_id, first_name, middle_name, last_name, gender, date_of_birth, place_of_birth, national_number, nationality, municipality, zip_code, street_name, number)
FROM '/docker-entrypoint-initdb.d/seeds/clients.csv' WITH CSV DELIMITER ',' QUOTE '"' HEADER;
