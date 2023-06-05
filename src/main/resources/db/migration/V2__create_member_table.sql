CREATE TABLE member
(
    id    VARCHAR(36)  NOT NULL,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    team_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE member
ADD CONSTRAINT fk_member_team
FOREIGN KEY (team_id)
REFERENCES team (id);