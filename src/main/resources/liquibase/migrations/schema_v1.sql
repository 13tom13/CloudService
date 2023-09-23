-- changeset tom13:1
create table cloudservice.cloud_user
(
    id       bigint NOT NULL AUTO_INCREMENT,
    login    varchar(255) DEFAULT NULL,
    password varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);


