@echo off

cd src
javac CreateDatabases.java
javac App.java
javac ./DBManager/Data.java
javac ./DBManager/DBConnection.java
javac ./DBManager/HorizontalPartitioningManager.java
javac ./DBManager/Sharding.java
javac ./DBManager/Caching.java

java -cp ".;mysql-connector-j-8.3.0.jar" CreateDatabases