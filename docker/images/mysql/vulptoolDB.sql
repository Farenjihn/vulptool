USE vulptool;

SET FOREIGN_KEY_CHECKS = 0;
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
    main_pseudo VARCHAR(255),
    auth_code VARCHAR(255) NOT NULL,
    access_code VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,

    PRIMARY KEY (id)
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

INSERT INTO player (main_pseudo, auth_code, access_code) VALUES ("test player", "AUTHCODE", "ACCESSCODE");
INSERT INTO figure (name, fclass, lvl, ilvl, player_id) VALUES ("test figure", "DeathKnight", 0, 0, 1);
INSERT INTO raid (name, nb_boss, difficulty ) VALUES ("test raid", 0, "Mythic");
INSERT INTO roster (name) VALUES ("test roster");
INSERT INTO meeting (time_begin, time_end) VALUES (NOW(), DATE_ADD(NOW(), INTERVAL 3 HOUR));
INSERT INTO event (name, description, raid_id, meeting_id, roster_id) VALUES ("test event", "some description", 1, 1, 1);
INSERT INTO figure_roster (figure_id, roster_id) VALUES (1, 1);
