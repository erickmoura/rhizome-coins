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
 
Run `mvn package` will run the tests, generate the fat jar and Elastic Beanstalk's zip file.

## Running pending migrations 
 
Run `java -jar target/rhizome-coins.jar db migrate config.yml` 
 
## Running the tests 
 
## Deployment 
