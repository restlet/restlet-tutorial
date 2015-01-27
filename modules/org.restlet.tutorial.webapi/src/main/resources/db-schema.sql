DROP table IF EXISTS Contact;
DROP table IF EXISTS Company;

CREATE TABLE Company (
    id INT AUTO_INCREMENT,
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

INSERT INTO Company (duns, name, website) values
  ('738673861', 'Rest!let', 'restlet.com'),
  ('947394731', 'Google', 'google.com')
;