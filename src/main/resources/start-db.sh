docker run --name sge-db -e MYSQL_ROOT_PASSWORD=javaee -e MYSQL_DATABASE=sge -d mysql

echo "para conectarte desde la consola del contenedor ejecuta: `mysql -uroot -pjavaee`"