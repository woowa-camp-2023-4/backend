drop table if exists MEMBERS;
drop table if exists PRODUCTS;

create table if not exists MEMBERS
(
    member_id bigint auto_increment not null,
    name varchar(255) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null,
    primary key(member_id)
);

create table if not exists PRODUCTS
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    image_url varchar(255),
    price bigint not null,
    status varchar(255) not null,
    quantity bigint not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);
