# Sistema de gestion de eventos

#### [Documentación online inicial](https://app.swaggerhub.com/apis-docs/flo00008/SGE-API/1.0.0#/) API Rest

#### [Dcoumentación online generada automáticamente](https://app.swaggerhub.com/apis-docs/flo00008/SGE-API/1.0.1) API Rest


# Instalación

Para desplegar la aplicación es necesaria una base de datos
Se necesita una base de datos mysql, el servidor necesita comunicarse con el puerto  __3306__  y tener usuario  __sge_user__  y contraseña  __javaee__ .

Existe un archivo [start-db.sh](https://gitlab.ujaen.es/cursoJEE/equipo3/SistemaGestionEventos/tree/master-frontend/src/main/resources/start-db.sh) que genera un contenedor docker con esta base de datos. 

Configuración dentro del fichero [application.yml](https://gitlab.ujaen.es/cursoJEE/equipo3/SistemaGestionEventos/blob/master-frontend/src/main/resources/application.yml). Se puede configurar si se desea volver a crear la base de datos con cada despliege.


Con los valores por defecto, al arrancar la aplicación se despliega una aplicación web en la url [http://localhost:12021/sge-api/](http://localhost:12021/sge-api/), y un servicio rest en la url [http://localhost:12021/sge-api/rest](http://localhost:12021/sge-api/rest) para documentación acceder a los enlaces de documentación online (para comprobar si está disponible cada servicio tiene un endpoint GET /ping ej: [http://localhost:12021/sge-api/rest/usuario/ping](http://localhost:12021/sge-api/rest/usuario/ping)). 


Para empaquetar la aplicación puede usarse el comando `mvn package spring-boot:repackage`, eso generará el .jar completo para ejecutar y que se podría meter en un docker (con dependencia de la base de datos).


# Tests
Para la ejecución de los test existe una base de datos H2 para que se realicen de manera autocontenida. Y no se necesita la base de datos mysql. Independientemente de esto la mayoria de tests no acceden a la base de datos ya que usan Mockito, excepto los de integración y sistema.