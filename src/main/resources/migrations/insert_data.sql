--liquibase formatted sql

--changeset omaida:1

insert into users(user_id, user_name)
values(1, "bot");

insert into exchanges(exchange_id,exchange_name, xchange_name, taker, maker, polling_rate)
values(1, "Poloniex", "org.knowm.xchange.poloniex.PoloniexExchange", 0.25, 0.15, 20);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (2, "ANXPRO", "org.knowm.xchange.anx.v2.ANXExchange", 0.6, 0.3, 30);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (3, "Bittrex", "org.knowm.xchange.bittrex.v1.BittrexExchange", 0.6, 0.3, 30);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (4, "Bitstamp", "org.knowm.xchange.bitstamp.BitstampExchange", 0.6, 0.3, 30);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (5, "BitFinex", "org.knowm.xchange.bitfinex.v1.BitfinexExchange", 0.6, 0.3, 30);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (6, "Coinbase", "org.knowm.xchange.coinbase.CoinbaseExchange", 0.6, 0.3, 30);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (7, "BTC-e", "org.knowm.xchange.btce.v3.BTCEExchange", 0.6, 0.3, 30);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (8, "Kraken", "org.knowm.xchange.kraken.KrakenExchange", 0.6, 0.3, 30);

insert into exchanges(exchange_id, exchange_name, xchange_name, taker, maker, polling_rate)
values (9, "BTCChina", "org.knowm.xchange.btcchina.BTCChinaExchange", 0.6, 0.3, 30);

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "Poloniex";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "ANXPRO";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "Bittrex";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "Bitstamp";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "BitFinex";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "Coinbase";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "BTC-e";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "Kraken";

insert into users_exchanges(user_id, exchange_id, p_key, secret)
select u.user_id, e.exchange_id, "kkkk", "ssss"
from users u, exchanges e
where user_name = "bot" and exchange_name = "BTCChina";


-- rollback truncate table users_exchanges
-- rollback truncate table exchanges
-- rollback truncate table users