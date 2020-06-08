docker run --name sge-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=sge -e MYSQL_USER=sge_user -e MYSQL_PASSWORD=javaee -d mysql

echo "para conectarte desde la consola del contenedor ejecuta: `mysql -uroot -pjavaee`"