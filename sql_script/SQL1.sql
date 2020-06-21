/*
SQL1: create tables;
*/

--- Clear constraints and tables first.
ALTER TABLE credential DROP CONSTRAINT credential_person_fk;
ALTER TABLE memoir DROP CONSTRAINT memoir_person_fk;
ALTER TABLE memoir DROP CONSTRAINT memoir_cinema_fk;
DROP TABLE memoir;
DROP TABLE credential;
DROP TABLE person;
DROP TABLE cinema;

---Not sure the gender settings, so keep it to a numeric value for furthur convenience.
CREATE TABLE person (
    personid INT PRIMARY KEY,
    fname VARCHAR(16) NOT NULL,
    lname VARCHAR(16) NOT NULL,
    gender INT NOT NULL,
    dob DATE NOT NULL,
    address VARCHAR(50) NOT NULL,
    stat VARCHAR(16) NOT NULL,
    postcode VARCHAR(4) NOT NULL
);

---One user has only one credential, so its primary key is the user id
CREATE TABLE credential (
    credentialid INT PRIMARY KEY,
    email VARCHAR(36) NOT NULL,
    hashedPw VARCHAR(64) NOT NULL,
    signupDate DATE NOT NULL,
    personid INT NOT NULL
);

---Use int to store score, and it will be converted to star in Java code.
CREATE TABLE memoir (
    memoirid INT PRIMARY KEY,
    movieName VARCHAR(30) NOT NULL,
    movieReleaseDate DATE NOT NULL,
    watchTime TIMESTAMP NOT NULL,
    comment VARCHAR(140),
    score REAL NOT NULL,
    personId INT NOT NULL,
    cinemaId INT NOT NULL,
    movieId INT NOT NULL,
    CHECK (score >= 0 AND score <= 100)
);

CREATE TABLE cinema (
    cinemaid INT PRIMARY KEY,
    name VARCHAR(30),
    postcode VARCHAR(4)
);

ALTER TABLE credential
ADD CONSTRAINT credential_person_fk
FOREIGN KEY (personid)
REFERENCES person(personid)
ON DELETE CASCADE;

ALTER TABLE memoir
ADD CONSTRAINT memoir_person_fk
FOREIGN KEY (personId)
REFERENCES person(personid)
ON DELETE CASCADE;

---When a cinema is deleted, the memoir record should be kept.
ALTER TABLE memoir
ADD CONSTRAINT memoir_cinema_fk
FOREIGN KEY (cinemaId)
REFERENCES cinema(cinemaid)
ON DELETE CASCADE;
