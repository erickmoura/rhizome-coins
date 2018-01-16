--liquibase formatted sql

--changeset omaida:1

create table exchanges(
    id bigint not null auto_increment primary key,
    exchange_name varchar(60),
    xchange_name text,
    taker float,
    maker float, 
    polling_rate int
);

alter table user_exchanges change exchange exchange_id int;
alter table user_exchanges modify user_id int;
alter table user_exchanges add p_key text not null;
alter table user_exchanges add secret text not null;

-- rollback alter table modify user_id numeric not null;
-- rollback alter table user_exchanges change exchange_id exchange varchar(10) not null;
-- rollback drop table exchanges;
