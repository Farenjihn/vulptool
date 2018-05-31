/*
Remarques:
- Certains noms de tables étaient des mots réservés, j'ai donc du en changer (cf commentaires)
- logs et saved_template ont des elements d'autres tables (Ex: event de type Event dans saved_template). Je suis partie du principe qu'on ne supprime pas vraiment
	les entrées: on leur met un booléen delete, s'il est a true il est "inexistant" mais l'élément existe quand même.
	Du coup il faudra une logique qui vérifie à la suppression que tout ce qui doit avoir ce booléen à true l'aie.
*/

DROP DATABASE IF EXISTS vulptool;
CREATE DATABASE vulptool;
USE vulptool;

CREATE TABLE raid(
	id INT NOT NULL AUTO_INCREMENT,
    raid_name VARCHAR(255) NOT NULL,
    nb_boss INT NOT NULL,
    difficulty ENUM('raid finder', 'normal mode', 'hard mode', 'mythic mode') NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE meeting(
	id INT NOT NULL AUTO_INCREMENT,
	meeting_date DATE NOT NULL,
    meeting_time TIME NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE roster(
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    
	PRIMARY KEY (id)
);

CREATE TABLE player(
	main_pseudo VARCHAR(255),
    token VARCHAR(255) NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (token)
);

CREATE TABLE event(
	id INT NOT NULL AUTO_INCREMENT,
	event_type ENUM('') NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    raidFK_id INT NOT NULL,
    meetingFK_id INT NOT NULL,
    rosterFK_id INT NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (raidFK_id) REFERENCES raid (id),
    FOREIGN KEY (meetingFK_id) REFERENCES meeting (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id) ON UPDATE CASCADE
);

CREATE TABLE event_child(
	id INT NOT NULL AUTO_INCREMENT,
    parent_id INT NOT NULL,
    rosterFK_id INT NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id),
    FOREIGN KEY (parent_id) REFERENCES event (id)
);

CREATE TABLE saved_template(
	id INT NOT NULL AUTO_INCREMENT,
    saved_event_fkid INT NOT NULL,
    rosterFK_id INT NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (saved_event_fkid) REFERENCES event (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id),
);

CREATE TABLE figure( /* remplace character*/
	id INT NOT NULL AUTO_INCREMENT,
    figure_name VARCHAR(255) NOT NULL,
    fclass VARCHAR(255) NOT NULL,
    lvl INT NOT NULL,
    ilvl FLOAT NOT NULL,
    playerFK_id VARCHAR(255) NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (playerFK_id) REFERENCES player (token)
);

CREATE TABLE figure_roster(
	figureFK_id INT NOT NULL,
    rosterFK_id INT NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    FOREIGN KEY (figureFK_id) REFERENCES figure (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id)
);


CREATE TABLE record( /*remplace logs*/
	id INT NOT NULL AUTO_INCREMENT,
    recorded_date DATE NOT NULL,
    eventFK_id INT NOT NULL,
    rosterFK_id INT NOT NULL,
    raidFK_id INT NOT NULL,
	is_deleted BOOLEAN NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (eventFK_id) REFERENCES event (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id),
    FOREIGN KEY (raidFK_id) REFERENCES raid (id)
);
