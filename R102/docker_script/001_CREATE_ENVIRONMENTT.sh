docker network create batch_network

docker run -d --name docker_batch_db \
    --network docker_batch_network \
    -e MYSQL_DATABASE=r102 \
    -e MYSQL_USER=user01 \
    -e MYSQL_PASSWORD=user01 \
    -e MYSQL_ROOT_PASSWORD=password \
    -v /Users/seungchulpark/APP/db/mysql:/var/lib/mysql \
    mysql