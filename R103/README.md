## 다중 디비 BATCH ##

docker run -d  --name mysqlDB2 \
-e MYSQL_DATABASE=r103 \
-e MYSQL_USER=user01 \
-e MYSQL_PASSWORD=user01 \
-e MYSQL_ROOT_PASSWORD=password \
-v /app/mysql2:/var/lib/mysql \
-p 23306:3306 \
mysql

create schema r103;
create user 'user01'@'%' identified by 'user01';
grant all privileges on r103.* to 'user01'@'%';