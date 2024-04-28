## CS 63690 Project
Matthew Chimitt
Ali Siddiqui
Asim Abdul Bari
Alvin Anto
Sai Kiran Banavath

## About
Our project was created in a Windows environment, utilizing MySQL and Java.
In this project we are implementing possible solutions to problems that arise from database scaling.
The solutions that we are implementing are
- Horizontal Partitioning
- Database Sharding
- Optimized Indexing
- Database Caching
Upon running our project, each of the four optimization strategies used on CRUD operations, (Inserting, Updating, Deleting, and Retrieving), 
and the running time of each compared against an original, unoptimized database.

## Setup MySQL
In order to run our code, MySQL is required.
MySQL can be downloaded from the following link:
https://dev.mysql.com/downloads/installer/
- Following the download, running the MSI installer will bring up the installation page.
- When installing, select "Server" (you can also select "Full", just make sure that the server is installed), then continue to select next until MySQL prompts you to enter a "MySQL Root Password".
- Create a password, and remember it, as it will be used to run the program. 
- Continue to select next and execute when necessary. Continue until it is complete and closes the install wizard. 

## Running the Program
To run the program, a the Databases must be created.
- This can be done by running the setup.bat script, which can be done by running the following command: **./Setup**
- This will prompt you to enter your MySQL password that was setup when you setup MySQL.
- Following the entry of your password, the necessary Java files will be compiled, and the CreateDatabases class will be run, creating all of the necessary databases and tables needed.
- Once the program terminates, the Run.bat file can be executed by running the following command in the terminal: **./Run** 
- Running this script will run the App class, which will against prompt the user for their MySQL password and will then run the main program.

Alternatively, this can be run without using the batch files:
- Navigate into the src folder
- Run the following commands to setup the datbases and tables (prompts for the MySQL password):
- javac CreateDatabases.java
- java -cp ".;mysql-connector-j-8.3.0.jar" CreateDatabases
- Run the following commands to run the main program (prompts for the MySQL password):
- javac App.java
- java -cp ".;mysql-connector-j-8.3.0.jar" App