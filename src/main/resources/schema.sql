drop table if exists MEMBERS;
drop table if exists PRODUCTS;
drop table if exists STOCKS;

create table if not exists MEMBERS
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    primary key(id)
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

create table if not exists STOCKS
(
    id bigint auto_increment not null,
    expiry_date date not null,
    quantity bigint not null,
    product_id bigint not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);