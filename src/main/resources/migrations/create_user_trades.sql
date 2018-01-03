--liquibase formatted sql

--changeset omaida:1

create table user_trades (
    id bigint key not null auto_increment,
    user_id int,
    exchange_id int,
    trade_id text,
    order_id text,
    currency_pair varchar(10),
    fee_amount double precision,
    fee_currency varchar(20),
    tradable_amount double precision,
    price double precision,
    trade_date datetime,
    trade_type varchar(10)
);

-- rollback drop table user_trades;
