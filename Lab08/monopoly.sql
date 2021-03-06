-- This SQL script builds a monopoly database, deleting any pre-existing version.
--
-- @author kvlinden
-- @version Summer, 2015
--
-- Caleb Boraby



DROP TABLE IF EXISTS PlayerGame CASCADE;
DROP TABLE IF EXISTS Game CASCADE;
DROP TABLE IF EXISTS Player CASCADE;
DROP TABLE IF EXISTS PlayerState CASCADE;
DROP TABLE IF EXISTS HousingSituation CASCADE;
DROP TABLE IF EXISTS GameState CASCADE;



-- Create the schema.
CREATE TABLE Game (
	ID integer PRIMARY KEY, 
	time timestamp
	);

CREATE TABLE Player (
	ID integer PRIMARY KEY, 
	emailAddress varchar(50) NOT NULL,
	name varchar(50)
	);

CREATE TABLE PlayerGame (
	gameID integer REFERENCES Game(ID), 
	playerID integer REFERENCES Player(ID),
	score integer
	);

-- Indicates the game that is going on and how much time has gone by so far
CREATE TABLE GameState (
	gameID integer REFERENCES Game(ID) PRIMARY KEY,
	timeSoFar timestamp
);

-- Indicated which game, who owns the space, which space it is, and how many houses are on it (0=none, 1-4=number of houses, 5=hotel)
CREATE TABLE HousingSituation (
	gameID integer REFERENCES Game(ID)
	, playerID integer REFERENCES Player(ID)
	, spaceID integer
	, houseCount integer
--	, CONSTRAINT spaceID CHECK (spaceID BETWEEN 1 AND 40)
--	, CONSTRAINT houseCount CHECK (houseCount BETWEEN 0 AND 5)
);

-- Indicates who it is, what game they're in, how much money they have, and where they are at in the game
CREATE TABLE PlayerState (
	playerID integer REFERENCES Player(ID)
	, gameID integer REFERENCES GameState(gameID)
	, cashTotal integer
	, currentSpot integer
--	, CONSTRAINT currentSpot CHECK (currentSpot BETWEEN 1 AND 40)
);

-- Allow users to select data from the tables.
GRANT SELECT ON Game TO PUBLIC;
GRANT SELECT ON Player TO PUBLIC;
GRANT SELECT ON PlayerGame TO PUBLIC;
GRANT SELECT ON GameState TO PUBLIC;
GRANT SELECT ON HousingSituation TO PUBLIC;


-- Add sample records.
INSERT INTO Game VALUES (1, '2006-06-27 08:00:00');
INSERT INTO Game VALUES (2, '2006-06-28 13:20:00');
INSERT INTO Game VALUES (3, '2006-06-29 18:41:00');
INSERT INTO Game VALUES (4, '2018-10-25 08:00:00');

INSERT INTO Player(ID, emailAddress) VALUES (1, 'me@calvin.edu');
INSERT INTO Player VALUES (2, 'king@gmail.edu', 'The King');
INSERT INTO Player VALUES (3, 'dog@gmail.edu', 'Dogbreath');

INSERT INTO PlayerGame VALUES (1, 1, 0.00);
INSERT INTO PlayerGame VALUES (1, 2, 0.00);
INSERT INTO PlayerGame VALUES (1, 3, 2350.00);
INSERT INTO PlayerGame VALUES (2, 1, 1000.00);
INSERT INTO PlayerGame VALUES (2, 2, 0.00);
INSERT INTO PlayerGame VALUES (2, 3, 500.00);
INSERT INTO PlayerGame VALUES (3, 2, 0.00);
INSERT INTO PlayerGame VALUES (3, 3, 5500.00);
INSERT INTO PlayerGame VALUES (4, 2, 100.00);

--Inserts for Lab07
INSERT INTO GameState VALUES (1, '2006-06-27 07:00:00');

INSERT INTO PlayerState VALUES (2, 1, 100, 37);
INSERT INTO PlayerState VALUES (3, 1, 300, 25);

INSERT INTO HousingSituation VALUES (1, 2, 37, 0);
INSERT INTO HousingSituation VALUES (1, 2, 3, 5);
INSERT INTO HousingSituation VALUES (1, 2, 16, 2);

INSERT INTO HousingSituation VALUES (1, 3, 12, 2);
INSERT INTO HousingSituation VALUES (1, 3, 25, 3);
INSERT INTO HousingSituation VALUES (1, 3, 40, 1);

-- Lab08
-- 8.1


-- a.
SELECT * FROM Game
ORDER BY time DESC;

-- b.

SELECT * FROM Game
WHERE time BETWEEN (DATE(NOW()) - 7) AND DATE(NOW());

-- c.
SELECT * FROM Player
WHERE name IS NOT NULL;

-- d.
SELECT DISTINCT playerID FROM PlayerGame
WHERE score > 2000;

-- e.
SELECT * FROM Player
WHERE emailAddress LIKE '%@gmail%';


-- 8.2

-- a.
SELECT * FROM PlayerGame
JOIN Player ON PlayerGame.playerID = Player.ID
WHERE Player.name = 'The King'
ORDER BY score DESC;

-- b.
SELECT name FROM Player
JOIN PlayerGame ON Player.ID = PlayerGame.playerID
JOIN Game ON PlayerGame.gameID = Game.ID
WHERE score = (SELECT MAX(score) FROM PlayerGame
					JOIN Game ON PlayerGame.gameID = Game.ID
				WHERE time = '2006-06-28 13:20:00');

-- c.
-- The P1.ID < P2.ID clause checks if the players in P1 and P2 (who have the same name) are the same person.
--	If the IDs for the players is the same then it won'e be selected since P1.ID = P2.ID (not <).

-- d.
-- You would want to to join a table to itself if the data within it references itself (i.e. an employee table where a supervisorID column points to the
--		the employee that is the boss of the current employee).
-- If values in a column need to be compared to other values in the same column, you use this Self-Join.
