DROP table IF EXISTS Contact;
DROP table IF EXISTS Company;

CREATE TABLE Company (
    id INT NOT NULL,
    duns VARCHAR(9) UNIQUE,
    name VARCHAR(255),
    address VARCHAR(255), 
    zip_code VARCHAR(5), 
    company_creation Date, 
    website VARCHAR(255), 
    phone_number VARCHAR(255), 
    city VARCHAR(255), 
    PRIMARY KEY(id)
);

CREATE TABLE Contact (
    id VARCHAR(255) NOT NULL,
    email VARCHAR (255) NOT NULL UNIQUE,
    age INT,
    name VARCHAR (255),
    firstname VARCHAR (255),
    company_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY (company_id) REFERENCES Company(id)
); 

INSERT INTO Company (id, duns, name, website) values
  (1, '7386', 'Restlet', 'restlet.com'),
  (2, '9473', 'Google', 'google.com')
;