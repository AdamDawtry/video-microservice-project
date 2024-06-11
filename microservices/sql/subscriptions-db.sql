# Sets up an extra database
create database subscriptions_database;

# Creates a user with all privileges on this database, with a certain password
grant all privileges on subscriptions_database.* to 'todo'@'%' identified by 'todosecret';