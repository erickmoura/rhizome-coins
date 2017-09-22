# RHIZOME-COINS 
 
project's description 
 
## Getting Started 
 
1. Clone the project 
2. Run `mvn clean install` to build the application 
3. Start application with `java -jar target/RhizomeCoins-jar-with-dependencies.jar server config.yml` 
 
 
### Prerequisites 
 
Java  
Mysql - version 5.7.19 
AWS console   
 
### Installing 
 
1. In `~/.aws/credentials` write  
 
[bot_cc_1] 
aws_access_key_id=AKIAJXBIVG4NYZWB37PA 
aws_secret_access_key=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  
 
2.  
 
## Running pending migrations 
 
Run `java -jar target/RhizomeCoins-jar-with-dependencies.jar db migrate config.yml` 
 
## Running the tests 
 
## Deployment 
