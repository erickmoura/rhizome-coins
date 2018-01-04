# RHIZOME-COINS  
  
Rhizome-coins is a Java-based backend that allows to connect to multiple wallets in order to collect information about user's balances and transfer history. Also retrieve balances and orders from importants exchanges. 
 
 
## Setup 
  
1. Install Java 1.8 
2. Install Maven 
3. Install MySql - version 5.7.19  
4. Install AWS  
4. Create database `rhizomecoins` 
5. `git clone https://github.com/erickmoura/rhizome-coins.git `
6. `cd rhizome-coins` 
7. Configure database username and password in config.yml file 
 
        database:  
            driverClass: com.mysql.jdbc.Driver 
            user:  USER 
            password:  PASSWORD 
            url: jdbc:mysql://localhost:3306/rhizomecoins 
 
8. Define in user-config.yml the key and secret to access to each exchange. Following the examples 
         
        username: my_name 
        Bittrex: 
            key: my_key 
            secret: my_secret 
        Poloniex: 
            key: my_key_2 
            secret: my_secret_2 
 
9. Build with Maven  
     
    `mvn clean install` 
 
10. Initialize the database with:  
     
    `java -jar target/rhizome-coins.jar db migrate config.yml` 
11. To seed the database with your information run: 
 
    `java -jar target/rhizome-coins create config.yml` 
12. Start  server application with : 
     
    `java -jar target/rhizome-coins.jar server config.yml`  
 
## How to use it 

### Doing requests for balances, trades, orders... 
 
Once the server starts the user balances, orders and user trades are stored in database.  
 
To get the balance of a date use: `http://0.0.0.0:8080/user/balances?collectDate=2017-10-25 0:00:00` 
 
To get all the orders between two dates use: `http://0.0.0.0:8080/user/orders?startDate=1969-12-31 00:00:00&endDate=2017-10-23 23:00:00 `
 
To get the trades between two dates use: `http://0.0.0.0:8080/user/trades?startDate=1969-12-31 00:00:00&endDate=2017-10-23 23:00:00 `
 
### Doing requests for balances and orders from important exchanges... 
 
The information is stored in Elastic Search and it's delivered by Amazon Kinesis Firehose. 
 
Working in the solution!!! 
 
## Wiki 
 
[Home](https://github.com/erickmoura/rhizome-coins/wiki) 
 
[Architecture and design notes](https://github.com/erickmoura/rhizome-coins/wiki/Architecture-and-design-notes) 
 
[List of coins](https://github.com/erickmoura/rhizome-coins/wiki/List-of-coins). 
 
[List of exchanges](https://github.com/erickmoura/rhizome-coins/wiki/List-of-exchanges)
