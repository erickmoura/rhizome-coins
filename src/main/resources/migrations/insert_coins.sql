--liquibase formatted sql

--changeset omaida:1

insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("bitcoin", "Bitcoin", "BTC", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("litecoin", "Litecoin", "LTC", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("dash", "Dash", "DASH", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("ripple", "Ripple", "XRP", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("monero", "Monero", "XMR", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("neo", "NEO", "NEO", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("tenx", "TenX", "PAY", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("monaco", "Monaco", "MCO", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("district0x", "district0x", "DNT", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("gas", "Gas", "GAS", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("steem", "Steem", "STEEM", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("cofound-it", "Cofound.it", "CFI", CURRENT_DATE, null);        
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("ethereum", "Ethereum", "ETH", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("lisk", "Lisk", "LSK", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("dogecoin", "Dogecoin", "DOGE", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("civic", "Civic", "CVC", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("tokencard", "TokenCard", "TKN", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("synereo", "Synereo", "AMP", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("digibyte", "DigiByte", "DGB", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("bitcoindark", "BitcoinDark", "BTCD", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("numeraire", "Numeraire", "NMR", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("bitconnect", "BitConnect", "BCC", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("ark", "Ark", "ARK", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("pinkcoin", "PinkCoin", "PINK", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("diamond", "Diamond", "DMD", CURRENT_DATE, null);
insert into coins(id, coin_name, symbol, inserted_date, removed_date) values("opus", "Opus", "OPT", CURRENT_DATE, null);

-- rollback truncate coins