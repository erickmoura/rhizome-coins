select user_name, user_id,exchange_id, exchange_name, xchange_name, taker, maker, polling_rate,p_key, secret
from user_exchanges
inner join users u on u.id = user_exchanges.user_id
inner join exchanges e on e.id = user_exchanges.exchange_id
where user_name = "bot"