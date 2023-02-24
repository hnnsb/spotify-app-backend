# Create a spring user for the db

+ Connect to docker
    - `docker exec -it <container> bash`
+ Login to mysql
    - `mysql --password`
+ Create User for spring app
    + Create the database:
        - `mysql> create database collectify;`
    + Create the user
        + `mysql> create user 'springuser'@'%' identified by 'springpassword';`
    + Grant permissions
        + `mysql> grant all on collectify.* to 'springuser'@'%';`
