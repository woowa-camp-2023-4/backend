drop table if exists MEMBERS;

create table if not exists MEMBERS
(
    member_id bigint auto_increment not null,
    name varchar(255) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null,
    primary key(member_id)
);
