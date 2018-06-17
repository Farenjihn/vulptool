USE vulptool;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS apitoken;
DROP TABLE IF EXISTS event_child;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS figure_roster;
DROP TABLE IF EXISTS figure;
DROP TABLE IF EXISTS meeting;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS raid;
DROP TABLE IF EXISTS record;
DROP TABLE IF EXISTS roster;
DROP TABLE IF EXISTS saved_template;
DROP EVENT IF EXISTS apitoken_cleanup;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE raid(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    nb_boss INT NOT NULL,
    difficulty ENUM('RaidFinder', 'Normal', 'Heroic', 'Mythic') NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id)
);

CREATE TABLE meeting(
    id INT NOT NULL AUTO_INCREMENT,
    time_begin TIMESTAMP NULL DEFAULT NULL,
    time_end TIMESTAMP NULL DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false DEFAULT false,

    PRIMARY KEY (id)
);

CREATE TABLE roster(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id)
);

CREATE TABLE player(
    id INT NOT NULL AUTO_INCREMENT,
    main_pseudo VARCHAR(255) NOT NULL,
    hashed_pw VARCHAR(255) NOT NULL,
    auth_code VARCHAR(255),
    access_code VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id)
);

CREATE TABLE apitoken(
    id INT NOT NULL AUTO_INCREMENT,
    player_id INT NOT NULL,
    value VARCHAR(255) NOT NULL,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    FOREIGN KEY (player_id) REFERENCES player (id)
);

CREATE TABLE event(
    id INT NOT NULL AUTO_INCREMENT,
    description TEXT NOT NULL,
    name VARCHAR(255) NOT NULL,
    raid_id INT NOT NULL,
    meeting_id INT NOT NULL,
    roster_id INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id),
    FOREIGN KEY (raid_id) REFERENCES raid (id),
    FOREIGN KEY (meeting_id) REFERENCES meeting (id),
    FOREIGN KEY (roster_id) REFERENCES roster (id) ON UPDATE CASCADE
);

CREATE TABLE event_child(
    id INT NOT NULL AUTO_INCREMENT,
    parent_id INT NOT NULL,
    roster_id INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id),
    FOREIGN KEY (roster_id) REFERENCES roster (id),
    FOREIGN KEY (parent_id) REFERENCES event (id)
);

CREATE TABLE saved_template(
    id INT NOT NULL AUTO_INCREMENT,
    saved_event_id INT NOT NULL,
    roster_id INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id),
    FOREIGN KEY (saved_event_id) REFERENCES event (id),
    FOREIGN KEY (roster_id) REFERENCES roster (id)
);

/* WoW character */
CREATE TABLE figure(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    fclass VARCHAR(255) NOT NULL,
    lvl INT NOT NULL,
    ilvl FLOAT NOT NULL,
    player_id INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id),
    FOREIGN KEY (player_id) REFERENCES player (id)
);

CREATE TABLE figure_roster(
    figure_id INT NOT NULL,
    roster_id INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    FOREIGN KEY (figure_id) REFERENCES figure (id),
    FOREIGN KEY (roster_id) REFERENCES roster (id)
);

CREATE TABLE record(
    id INT NOT NULL AUTO_INCREMENT,
    time DATE NOT NULL,
    event_id INT NOT NULL,
    roster_id INT NOT NULL,
    raid_id INT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES event (id),
    FOREIGN KEY (roster_id) REFERENCES roster (id),
    FOREIGN KEY (raid_id) REFERENCES raid (id)
);

CREATE EVENT apitoken_cleanup ON SCHEDULE EVERY 1 HOUR
DO DELETE FROM apitoken WHERE id IN (
    SELECT id FROM apitoken WHERE time_created < UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 5 DAY))
);

/*********************** DATA FOR DEMO **************************/

INSERT INTO player (main_pseudo, hashed_pw, auth_code, access_code) VALUES ("Bykow", "password", "234567689", "876543212");
INSERT INTO player (main_pseudo, hashed_pw, auth_code, access_code) VALUES ("Farenjihn", "sssssssssssssss", "423243543", "342343422");
INSERT INTO player (main_pseudo, hashed_pw, auth_code, access_code) VALUES ("Ardgevald", "sssssssssssssss", "234511289", "712323123");
INSERT INTO player (main_pseudo, hashed_pw, auth_code, access_code) VALUES ("Stelmaria", "sssssssssssssss", "567523423", "565767567");
INSERT INTO player (main_pseudo, hashed_pw, auth_code, access_code) VALUES ("Crazy", "sssssssssssssss", "567233423", "5651217567");
INSERT INTO player (main_pseudo, hashed_pw, auth_code, access_code) VALUES ("Fatcream", "sssssssssssssss", "2091380982", "5612127567");
INSERT INTO player (main_pseudo, hashed_pw, auth_code, access_code) VALUES ("Intox", "sssssssssssssss", "209131180982", "561222127567");

INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Asmodian", "DemonHunter", 10, 8, 1);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Kaendas", "Warrior", 55, 53, 1);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Aeran", "Paladin", 23, 20, 2);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Brohat", "Mage", 51, 51, 2);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Zmei", "DeathKnight", 18, 16, 3);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Apalala", "Druid", 53, 50, 3);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Earalith", "Hunter", 19, 16, 4);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Hyniel", "Shaman", 50, 45, 4);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Bykowpal", "Paladin", 110, 980, 1);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Crazydiamond", "Paladin", 110, 981, 5);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Fatcream", "Warlock", 110, 980, 6);
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("Intoxity", "Warlock", 110, 950, 7);


INSERT INTO raid (name, nb_boss, difficulty ) VALUES ("Antorus, le Trone ardent", 11, "Mythic");
INSERT INTO raid (name, nb_boss, difficulty ) VALUES ("Palais Sacrenuit", 11, "Heroic");
INSERT INTO raid (name, nb_boss, difficulty ) VALUES ("Le Cauchemar d emeraude", 7, "Mythic");
INSERT INTO raid (name, nb_boss, difficulty ) VALUES ("Tombe de Sargeras", 9, "Normal");


INSERT INTO roster (name) VALUES ("Main team");
INSERT INTO roster (name) VALUES ("Rescue team");
INSERT INTO roster (name) VALUES ("Test team");

INSERT INTO meeting (time_begin, time_end) VALUES ('2018-06-21 07:00:00', DATE_ADD('2018-06-21 07:00:00', INTERVAL 3 HOUR));
INSERT INTO meeting (time_begin, time_end) VALUES ('2018-06-17 08:00:00', DATE_ADD('2018-06-17 08:00:00', INTERVAL 5 HOUR));
INSERT INTO meeting (time_begin, time_end) VALUES ('2018-06-05 18:00:00', DATE_ADD('2018-06-05 18:00:00', INTERVAL 3 HOUR));
INSERT INTO meeting (time_begin, time_end) VALUES ('2018-06-29 07:00:00', DATE_ADD('2018-06-29 07:00:00', INTERVAL 7 HOUR));

INSERT INTO event (name, description, raid_id, meeting_id, roster_id) VALUES ("Antorus", "Full Antorus", 1, 1, 1);
INSERT INTO event (name, description, raid_id, meeting_id, roster_id) VALUES ("Sacrenuit", "Get the semperardent s equipment", 2, 2, 1);
INSERT INTO event (name, description, raid_id, meeting_id, roster_id) VALUES ("Cauchemar", "Revenge on Cenarius", 3, 3, 3);
INSERT INTO event (name, description, raid_id, meeting_id, roster_id) VALUES ("Sargeras", "Upgrading equipment", 4, 4, 2);

INSERT INTO event_child (parent_id, roster_id) VALUES (3, 1);
INSERT INTO event_child (parent_id, roster_id) VALUES (3, 1);
INSERT INTO event_child (parent_id, roster_id) VALUES (3, 1);
INSERT INTO event_child (parent_id, roster_id) VALUES (3, 1);
INSERT INTO event_child (parent_id, roster_id) VALUES (3, 2);
INSERT INTO event_child (parent_id, roster_id) VALUES (3, 2);
INSERT INTO event_child (parent_id, roster_id) VALUES (3, 2);

INSERT INTO saved_template (saved_event_id, roster_id) VALUES (1, 1);
INSERT INTO saved_template (saved_event_id, roster_id) VALUES (2, 1);
	
/*INSERT INTO record (time, event_id, roster_id, raid_id) VALUES (CURDATE(), 1, 1);*/

INSERT INTO figure_roster (figure_id, roster_id) VALUES (2, 1);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (4, 1);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (6, 1);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (8, 1);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (3, 2);	
INSERT INTO figure_roster (figure_id, roster_id) VALUES (1, 2);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (7, 2);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (1, 3);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (5, 3);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (8, 3);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (9, 3);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (10, 3);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (11, 3);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (12, 3);
