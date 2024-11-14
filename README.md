Sistema de Liquidación de Sueldos - Colegio de Contadores
Este proyecto es una aplicación de escritorio desarrollada en JavaFX que permite la gestión de trabajadores, la liquidación de sueldos y la generación de recibos de sueldo para los miembros del Colegio de Contadores de San Salvador. 
El sistema está diseñado para ser utilizado por administradores y usuarios, con funcionalidades específicas según el rol.

Tecnologías Utilizadas
Java 17: Lenguaje de programación principal.
JavaFX: Framework para el desarrollo de la interfaz gráfica de usuario.
MySQL: Sistema de gestión de bases de datos para el almacenamiento de información.
JDBC: Conexión entre la aplicación Java y la base de datos MySQL.
mysql-connector-java: Conector para integrar MySQL con Java.

Características Principales

Gestión de Trabajadores (Usuarios):

Los usuarios pueden agregar, editar y eliminar trabajadores.
Cada trabajador tiene información personal, como nombre, apellido, DNI, sueldo bruto, teléfono, y correo electrónico.


Cálculo de Sueldos:

Los sueldos se calculan en función del sueldo bruto y descuentos aplicables (alícuotas).
El sistema permite registrar alícuotas (descuentos) para cada trabajador.
Los sueldos netos se calculan automáticamente después de aplicar los descuentos.


Generación de Recibos de Sueldo:

El sistema genera un recibo de sueldo en formato PDF con los detalles de:
Nombre del trabajador.
Sueldo bruto.
Descuentos aplicados.
Sueldo neto.
Roles y Permisos:

Administradores: Tiene acceso completo a la gestión de usuarios.

Usuario: Solo puede gestionar los trabajadores asignados y generar recibos de sueldo.

Estructura de la Base de Datos
El sistema utiliza una base de datos MySQL con la siguiente estructura de tablas:

usuario:

id_usuario (INT, AUTO_INCREMENT)
nombre (VARCHAR(50))
apellido (VARCHAR(50))
dni (VARCHAR(10))
matricula (VARCHAR(20), opcional)
email (VARCHAR(50))
contrasena (VARCHAR(100), encriptada con hash)
rol (ENUM('admin', 'usuario'))
trabajador:

id_trabajador (INT, AUTO_INCREMENT)
nombre (VARCHAR(50))
apellido (VARCHAR(50))
dni (VARCHAR(10))
sueldo_bruto (DECIMAL(10, 2))
email (VARCHAR(50))
telefono (VARCHAR(15))
id_usuario (FK)
alicuota:

id_alicuota (INT, AUTO_INCREMENT)
descripcion (VARCHAR(100))
porcentaje (DECIMAL(5, 2))
id_trabajador (FK)
recibo:

id_recibo (INT, AUTO_INCREMENT)
id_trabajador (FK)
fecha (DATE)
sueldo_neto (DECIMAL(10, 2))
usuario_trabajador:

id_usuario_trabajador (INT, AUTO_INCREMENT)
id_usuario (FK)
id_trabajador (FK)
Instalación y Configuración
1. Requisitos Previos
JDK 17: Asegúrate de tener instalado Java 17.
MySQL: Necesitarás una instalación de MySQL y el conector mysql-connector-java para la conexión de la base de datos.
IntelliJ IDEA: Se recomienda usar IntelliJ IDEA como entorno de desarrollo para JavaFX.
2. Pasos para la instalación


Clona el repositorio:

bash
Copiar código
git clone https://github.com/usuario/proyecto-liquidacion-sueldos.git
Importa el proyecto en IntelliJ IDEA:

Abre IntelliJ IDEA y selecciona Open.
Selecciona la carpeta donde se clonó el proyecto.
Configura la base de datos:

Crea una base de datos llamada gestion_sueldos en MySQL.
Importa los scripts de base de datos incluidos en el proyecto (puedes encontrarlos en la carpeta scripts).
Configuración de la conexión JDBC:

En el archivo DatabaseConnection.java, configura los parámetros de la conexión a MySQL, como el usuario, la contraseña y la URL de conexión.
Asegúrate de que el archivo mysql-connector-java esté incluido en el proyecto.
Ejecuta el proyecto:

Corre la clase Main.java para iniciar la aplicación.

Uso de la Aplicación

Inicio de sesión:
Los administradores tienen acceso a traves de  usuario: juan.perez@example.com y contraseña: hashed_password_1

Los usuarios pueden iniciar sesión con usuario: maria.gonzalez@example.com y contraseña:  hashed_password_2

