-- create a new database
create database if not exists login_db;

-- select db
use login_db;

select * from roles;
select * from users;
select * from user_roles;


-- insert roles manually
insert into roles(role_id, name)
values(1, "manager"); 
insert into roles(role_id, name)
values(2, "user"); 

-- insert some admins 
insert into users(name, username, password)
values ("john smith", "jsmith", "$2a$10$1BqKNrliPyThXb/e0PVIkOFjXHj6xCVxsf5d75gjccd4KdDo8B8ui");
insert into user_roles(user_id, role_id)
values(1, 1);

insert into users(name, username, password)
values ("grover underwood", "grover", "$2a$10$1BqKNrliPyThXb/e0PVIkOFjXHj6xCVxsf5d75gjccd4KdDo8B8ui");
insert into user_roles(user_id, role_id)
values(2, 1);

-- show tables
select * from roles;
select * from users;
select * from user_roles;

-- reset
drop database login_db;
