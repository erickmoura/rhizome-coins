--liquibase formatted sql

--changeset omaida:1

create table coins(
    id varchar(20),
    coin_name varchar(60),
    symbol varchar(5),
    inserted_date datetime,
    removed_date datetime
);

-- rollback drop table coins;