drop table if exists MEMBERS;

create table if not exists MEMBERS
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    primary key(id)
);
