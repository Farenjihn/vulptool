DROP DATABASE IF EXISTS vulptool;
CREATE DATABASE vulptool;
USE vulptool;

CREATE TABLE raid(
	id INT NOT NULL AUTO_INCREMENT,
    raid_name VARCHAR(255) NOT NULL,
    nb_boss INT NOT NULL,
    difficulty ENUM('raid finder', 'normal mode', 'hard mode', 'mythic mode') NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE meeting(
	id INT NOT NULL AUTO_INCREMENT,
	meeting_date DATE NOT NULL,
    meeting_time TIME NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE roster(
	id INT NOT NULL AUTO_INCREMENT,
	type_roster VARCHAR(255) NOT NULL,
    
    
	PRIMARY KEY (id)
);

CREATE TABLE player(
	main_pseudo VARCHAR(255),
    token VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (token)
);

CREATE TABLE event(
	id INT NOT NULL AUTO_INCREMENT,
	event_type ENUM('') NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    raidFK_id INT NOT NULL,
    meetingFK_id INT NOT NULL,
    rosterFK_id INT NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (raidFK_id) REFERENCES raid (id),
    FOREIGN KEY (meetingFK_id) REFERENCES meeting (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id) ON UPDATE CASCADE
);

CREATE TABLE event_child(
	parent_id INT NOT NULL AUTO_INCREMENT,
    rosterFK_id INT NOT NULL,
    eventFK_id INT NOT NULL,
    
    PRIMARY KEY (parent_id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id),
    FOREIGN KEY (eventFK_id) REFERENCES event (id)
);

CREATE TABLE saved_template(
	id INT NOT NULL AUTO_INCREMENT,
    saved_event_fkid INT NOT NULL,
    rosterFK_id INT NOT NULL,
    raidFK_id INT NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (saved_event_fkid) REFERENCES event (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id),
    FOREIGN KEY (raidFK_id) REFERENCES raid (id)
);

CREATE TABLE figure( /* remplace character*/
	id INT NOT NULL AUTO_INCREMENT,
    figure_name VARCHAR(255) NOT NULL,
    classe VARCHAR(255) NOT NULL,
    lvl INT NOT NULL,
    playerFK_id VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (playerFK_id) REFERENCES player (token)
);

CREATE TABLE figure_roster(
	figureFK_id INT NOT NULL,
    rosterFK_id INT NOT NULL,
    
    FOREIGN KEY (figureFK_id) REFERENCES figure (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id)
);


CREATE TABLE record( /*remplace log*/
	id INT NOT NULL AUTO_INCREMENT,
    recorded_date DATE NOT NULL,
    eventFK_id INT NOT NULL,
    rosterFK_id INT NOT NULL,
    raidFK_id INT NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (eventFK_id) REFERENCES event (id),
    FOREIGN KEY (rosterFK_id) REFERENCES roster (id),
    FOREIGN KEY (raidFK_id) REFERENCES raid (id)
);
