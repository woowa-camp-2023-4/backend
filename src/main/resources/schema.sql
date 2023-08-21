drop table if exists MEMBERS;
drop table if exists PRODUCTS;
drop table if exists STOCKS;
drop table if exists ORDERS;
drop table if exists ORDER_ITEMS;
drop table if exists PAYMENTS;

create table if not exists MEMBERS
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255) not null,
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

CREATE TABLE ORDER_ITEMS
(
    order_id   bigint       not null,
    product_id bigint       not null,
    name       varchar(20)  not null,
    image      varchar(255) not null,
    price      int          not null,
    quantity   bigint          not null,
    item_index int
);

create table if not exists ORDERS
(
    id bigint auto_increment not null,
    order_status varchar(255) not null,
    total_price bigint not null,
    member_id bigint not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);
