docker run -p 3306:3306 --name sge-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=sge -e MYSQL_USER=sge_user -e MYSQL_PASSWORD=javaee -d mysql

echo "para conectarte desde la consola del contenedor ejecuta: `mysql -usge_user -pjavaee`"