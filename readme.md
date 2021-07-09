## Proyecto
Alpha
## Descripción
El programa que se está implementado para el ALPHA es una API desde el programa SpringTool y se crean un nuevo proyecto con las diferentes clases que lo componen, los dao's, service's y un controlador para así poder crear métodos HTTP. También tenemos instalado PostgreSQL para nuestra base de datos.
El postman simplmente lo ocupamos para hacer peticiones de los diferentes metodos que creamos en la API.
Como se va avanzando hemos creado otro programa en el cual ejecuta procesos en determiando tiempo del día, una de ellas es que con la ayuda de nuestra API y con este programa extrae información y lo agrega a nuestra base de datos y así tener un control de los clientes del alpha.
## Instalación 
Para la creación de este proyecto se intalarón los siguientes programas, todos los programas está para windows:
[SpringTool] https://spring.io/tools/
[PostgreSQL 13] https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
[Postman] https://www.postman.com/downloads/
## Uso
En el springtool debes de buscar el programa de la API, se debe de dar clic derecho al archivo ir a Run As y luego Spring Boot App y comenzará a ejecutarse, luego se deberá de ejecutar el otro archivo de QuartzMain y comenzará a extraer la información de los cliente, en el QuartzMain estará el endpoint, el metodo HTTP de nuestra API y todo lo que compone del .JSON de clientes, todas las ejecuciones se hacen al mismo tiempo.
## Autores
DANIEL GARCÍA VELASCO
ABIMAEL RUEDA GALINDO
