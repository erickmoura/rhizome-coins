#!/bin/bash

java -jar rhizome-coins.jar db migrate config.yml
java -jar rhizome-coins.jar server config.yml
