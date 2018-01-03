--liquibase formatted sql

--changeset omaida:1

alter table user_balances add collect_date datetime;

-- rollback alter table user_balances drop column user_balances;