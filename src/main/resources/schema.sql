drop table if exists MEMBERS;
drop table if exists PRODUCTS;
drop table if exists STOCKS;
drop table if exists ORDERS;
drop table if exists ORDER_ITEMS;
drop table if exists ORDER_ITEM_STOCKS;
drop table if exists PAYMENTS;
drop table if exists CART_ITEMS;

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
    stock_type varchar(255),
    created_at datetime not null,
    updated_at datetime not null,
    primary key (id)
);

CREATE TABLE if not exists ORDER_ITEMS
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



create table if not exists ORDERS
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

create table if not exists PAYMENTS
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

create table if not exists CART_ITEMS
(
    id bigint auto_increment not null,
    member_id bigint not null,
    product_id bigint not null,
    quantity   bigint not null,
    created_at datetime not null,
    updated_at datetime not null,
    primary key(id)
);
