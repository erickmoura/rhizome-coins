--liquibase formatted sql

--changeset omaida:1

create table user_balances (
    id bigint key not null auto_increment,
    user_id int,
    exchange_id int,
    currency varchar(10),
    total double precision,
    available double precision,
    frozen double precision,
    loaned double precision,
    borrowed double precision,
    withdrawing double precision,
    depositing double precision
);

create table user_orders (
    id bigint key not null auto_increment,
    order_id varchar(60),
    user_id int,
    exchange_id int,
    currency varchar(10),
    order_type varchar(20),
    order_status varchar(20),
    tradable_amount double precision,
    cumlative_amount double precision,
    average_price double precision,
    order_date datetime
);

alter table user_exchanges add id bigint key not null auto_increment;
alter table user_exchanges add last_updated_balances datetime;
alter table user_exchanges add last_updated_orders datetime;

-- rollback drop table user_balances;
-- rollback drop table user_orders;
-- rollback alter table user_exchanges drop column id;
-- rollback alter table user_exchanges drop column last_updated_balances;
-- rollback alter table user_exchanges drop column last_updated_orders;