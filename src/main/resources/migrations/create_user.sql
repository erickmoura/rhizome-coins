--liquibase formatted sql

--changeset omaida:1

create table users(
    id bigint not null auto_increment key,
    user_name varchar(60)
);

create table user_exchanges(
    user_id numeric not null,
    exchange varchar(10) not null,
    properties json
)

-- rollback drop table user_exchanges;
-- rollback drop table users;