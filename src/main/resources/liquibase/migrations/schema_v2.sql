-- changeset tom13:2
CREATE TABLE cloudservice.file_storage
(
    id       bigint NOT NULL AUTO_INCREMENT,
    created  datetime(6)  DEFAULT NULL,
    filename varchar(255) DEFAULT NULL,
    path     varchar(255) DEFAULT NULL,
    size     bigint NOT NULL,
    updated  datetime(6)  DEFAULT NULL,
    username varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
);



