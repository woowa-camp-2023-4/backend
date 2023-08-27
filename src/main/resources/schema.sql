drop table if exists members;
drop table if exists products;
drop table if exists stocks;
drop table if exists orders;
drop table if exists order_items;
drop table if exists payments;
drop table if exists cart_items;

create table if not exists members
(
    id bigint auto_increment not null,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(255) not null,
    primary key(id)
);

create table if not exists products
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

create table if not exists stocks
(
    id bigint auto_increment not null,
    expiry_date date not null,
    quantity bigint not null,
    product_id bigint not null,
    stock_type varchar(255),
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);

CREATE TABLE if not exists order_items
(
    id bigint auto_increment not null,
    order_id   bigint,
    product_id bigint       not null,
    name       varchar(20)  not null,
    image      varchar(255) not null,
    price      bigint          not null,
    quantity   bigint          not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);



create table if not exists orders
(
    id bigint auto_increment not null,
    order_status varchar(255) not null,
    total_price bigint not null,
    member_id bigint not null,
    uuid varchar(255) not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);

create table if not exists payments
(
    id bigint auto_increment not null,
    order_id bigint not null,
    payment_key varchar(255) not null,
    total_price bigint not null,
    uuid varchar(255) not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);

create table if not exists cart_items
(
    id bigint auto_increment not null,
    member_id bigint not null,
    product_id bigint not null,
    quantity   bigint not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key(id)
);

create table if not exists product_sales
(
    id bigint auto_increment not null,
    product_id bigint not null,
    sale bigint not null,
    sale_date   date not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key(id)
);
