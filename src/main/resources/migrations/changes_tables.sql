--liquibase formatted sql

--changeset omaida:1

alter table user_exchanges rename users_exchanges;
alter table exchanges change id exchange_id int;
alter table users change id user_id int;

-- rollback alter table users change user_id id int;
-- rollback alter table exchanges change exchange_id id int;
-- rollback alter table users_balances rename user_balances;