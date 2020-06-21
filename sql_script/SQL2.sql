DELETE FROM memoir;
DELETE FROM credential;
DELETE FROM person;
DELETE FROM cinema;

---Addresses come from 'Fake Address Generator'. Passwords are all '123456' before sha256 hash.
INSERT INTO person (personid, fname, lname, gender, dob, address, stat, postcode) VALUES(1, 'Alice', 'Smith', 2, DATE('1990-01-01'), '18 Parkes Road, Melbourne', 'VIC', '3004');

INSERT INTO person (personid, fname, lname, gender, dob, address, stat, postcode) VALUES(2, 'John', 'Card', 1, DATE('1972-02-05'), '25 Ocean Street, Sydney', 'NSW', '2000');

INSERT INTO person (personid, fname, lname, gender, dob, address, stat, postcode) VALUES(3, 'Hunter', 'Onslow', 1, DATE('1988-10-29'), '92 Mills Street, Brisbane', 'QLD', '4000');

INSERT INTO credential(credentialid, email, hashedPw, signupDate, personid)  VALUES (1,  'a@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', DATE('2018-11-15'), 1);

INSERT INTO credential(credentialid, email, hashedPw, signupDate, personid) VALUES (2, 'johncard123@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', DATE('2019-07-05'), 2);

INSERT INTO credential(credentialid, email, hashedPw, signupDate, personid) VALUES (3, 'greatpi@outlook.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', DATE('2020-01-1'), 3);

--- Cinema info from 'top 10 of the best Australian boutique cinemas'.
INSERT INTO cinema(cinemaid, name, postcode) VALUES(1, 'The Astor Theatre', '3183');

INSERT INTO cinema(cinemaid, name, postcode) VALUES(2, 'Chauvel Cin茅math猫que', '2021');

INSERT INTO cinema(cinemaid, name, postcode) VALUES(3, 'Hayden Orpheum Picture Palace', '2090');

INSERT INTO cinema(cinemaid, name, postcode) VALUES(4, 'The Regal Twin', '4075');

INSERT INTO cinema(cinemaid, name, postcode) VALUES(5, 'The State Cinema', '7000');

---Movie info from 'IMDB'.
INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(1, 'Onward', DATE('2020-03-06'), TIMESTAMP('2020-03-08 19:25:06'), 'Great movie, gotta watch it again!', 4, 1, 1, 508439);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(2, 'White Lines', DATE('2020-04-15'), TIMESTAMP('2020-05-11 10:37:11'), 'Just so so', 3.5, 1, 2, 90265);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(3, 'Onward', DATE('2020-03-06'), TIMESTAMP('2020-03-16 20:12:42'), 'Still interesting again.', 4.5, 1, 1, 508439);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(4, 'Invisible Man', DATE('2020-02-28'), TIMESTAMP('2020-03-03 16:07:25'), '', 4, 1, 1, 570670);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(5, 'Little Women', DATE('2019-12-25'), TIMESTAMP('2020-01-07 14:13:06'), '', 4, 1, 1,331482);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(6, '1917', DATE('2020-01-10'), TIMESTAMP('2020-01-12 18:52:36'), '', 4.5, 1, 1,530915);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(7, '1917', DATE('2020-01-10'), TIMESTAMP('2020-01-11 19:25:06'), 'This is awesome!', 4, 2, 2, 530915);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(8, '1917', DATE('2020-01-10'), TIMESTAMP('2020-01-15 14:37:19'), '', 4, 3, 4, 530915);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(9, 'Onward', DATE('2020-03-06'), TIMESTAMP('2020-03-11 22:12:31'), '', 4, 2, 3, 508439);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(10, 'Invisible Man', DATE('2020-02-28'), TIMESTAMP('2020-03-02 21:05:17'), 'WTF is it talking about?', 3, 2, 2, 570670);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(11, 'The Lion King', DATE('1994-06-23'), TIMESTAMP('2019-11-12 15:44:21'), '', 3, 2, 2, 8587);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(12, 'The Lion King', DATE('2019-07-19'), TIMESTAMP('2019-11-27 14:29:05'), '', 3.5, 2, 2, 420818);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(13, 'Never Rarely Sometimes Always', DATE('2020-01-24'), TIMESTAMP('2020-02-25 17:44:16'), '', 3.5, 1, 2, 595671);

INSERT INTO memoir(memoirid, movieName, movieReleaseDate, watchTime, comment, score, personId, cinemaId, movieId) VALUES(14, 'The Hunt', DATE('2020-03-13'), TIMESTAMP('2020-04-2 19:31:05'), '', 3, 1, 1,514847);


