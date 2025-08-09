# Instrucciones de Configuración y Ejecución del Proyecto

Siga estos pasos para configurar y ejecutar el proyecto:

## 1. Iniciar CockroachDB (Docker)

Primero, debe iniciar la instancia de CockroachDB usando Docker Compose. Navegue al directorio `cockroachdb` y ejecute el archivo Docker Compose:

```bash
cd cockroachdb
docker-compose up -d
```

Después de iniciar CockroachDB, debe crear las bases de datos necesarias. Conéctese a la instancia de CockroachDB y ejecute los siguientes comandos SQL:

```bash
# Conéctese a CockroachDB (asegúrese de que el cliente psql esté instalado y en su PATH)
psql -h localhost -p 26257 -U root --no-password -e "CREATE DATABASE auth_db; CREATE DATABASE catalogo_db; CREATE DATABASE notif_db; CREATE DATABASE pub_db;"
```

## 2. Colocar Clave Privada para el Servicio de Autenticación

Para que el `auth-service` funcione correctamente, debe colocar un archivo de clave privada en sus recursos. ingeniero en la carpeta compartida esta el archivo.

Copie el archivo de clave privada en el siguiente directorio:

`auth-service/src/main/resources/keys/`

## 3. Iniciar Microservicios

Los microservicios deben iniciarse en un orden específico. Navegue al directorio raíz de cada servicio y compílelo/ejecútelo. Generalmente, puede usar Maven para esto.

**Orden Importante:**

1.  **Servidor Eureka:** Inicie el `eureka-server` primero, ya que otros servicios se registran en él.
    ```bash
    cd eureka-server
    ./mvnw spring-boot:run
    ```
2.  **Otros Servicios:** Una vez que Eureka esté en funcionamiento, inicie el `auth-service`, `catalogo-service`, `notificaciones-service`, `observabilidad` y `publicaciones-service`. Puede iniciarlos en cualquier orden entre ellos.
    ```bash
    cd auth-service
    ./mvnw spring-boot:run
    # Repita para catalogo-service, notificaciones-service, observabilidad, publicaciones-service
    ```
3.  **Servicio Gateway:** Inicie el `gateway-service` al final, ya que enruta las solicitudes a todos los demás servicios.
    ```bash
    cd gateway-service
    ./mvnw spring-boot:run
    ```

## 4. Iniciar Frontend

Finalmente, configure e inicie la aplicación frontend.

1.  Navegue al directorio `frontend`:
    ```bash
    cd frontend
    ```
2.  Instale las dependencias necesarias de Node.js:
    ```bash
    npm install
    ```
3.  Inicie el servidor de desarrollo:
    ```bash
    npm run dev
    ```

¡Su proyecto debería estar completamente operativo ahora!