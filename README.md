# dxc_login
login interface assignment from DXC

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

## Default Accounts for testing
|type    |username  |password  |
|--------|----------|----------|
|manager |jsmith    |password  |
|manager |grover    |password  |
|user    |kellz     |password  |
- accounts registered using the interface will be ```user``` level accounts
