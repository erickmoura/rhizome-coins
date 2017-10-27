--liquibase formatted sql

--changeset omaida:1

alter table user_balances add user_balances datetime;

-- rollback alter table user_balances drop column user_balances;