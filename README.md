# dxc_login
login interface assignment from DXC. Default testing accounts will be added into the database when setting up(see below). Upon logging in, a web token will be saved in the form of a cookie for single sign on purposes. 

## Stack
- Angular
- Spring Boot (maven)
  
## Databse Setup
- import database dump file from backend > database > login_db_dump.sql
- default datasource connection (editable under application.properties)
  - username: root
  - password: password

## Frontend setup
- navigate to frontend > login
- run ```npm install``` to install packages
- run ```ng serve``` to start
- on the browser, navigate to ```http://localhost:4200/login``` 

## Default Accounts for testing
|type    |username  |password  |
|--------|----------|----------|
|manager |jsmith    |password  |
|manager |grover    |password  |
|user    |kellz     |password  |
- accounts registered using the interface will be ```user``` level accounts
- registration and login also supports English and Chinese
