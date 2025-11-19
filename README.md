# Taller Final - Seguridad con Apache Shiro

## Integrantes
- Persona 1: [Tu nombre]
- Persona 2: [Nombre del compañero]

---

## Descripción

Proyecto desarrollado en Java (Maven) que implementa:
- Un CRUD básico de productos.
- Integración real con Apache Shiro para login y control de acceso.
- Registro de usuarios con contraseñas hasheadas utilizando bcrypt.
- Control de sesión, cierre, y validación de roles.

---

## Requisitos Cubiertos

- ✔️ CRUD funcional sin seguridad ni restricciones.
- ✔️ Integración de Apache Shiro (SecurityManager, IniRealm configurado con usuarios y roles).
- ✔️ Login seguro con `Subject.login(token)` de Shiro.
- ✔️ Contraseñas protegidas con bcrypt, nunca en texto plano.
- ✔️ Registro de usuarios desde la app, persistidos en archivo plano tipo BD (`usuarios.json`).
- ✔️ Evidencias de funcionamiento: login, registro, control de sesiones.

---

## Ejecución del proyecto

1. **Clona el repositorio:**

-- git clone https://github.com/tuusuario/taller-final-seguridad-shiro.git
-- cd taller-final-seguridad-shiro

2. **Compila el proyecto:**

-- mvn clean install

-- cd v1
-- mvn exec:java -Dexec.mainClass="uptc.edu.co.App"


4. **Ingresa con usuario de ejemplo (del archivo `shiro.ini`):**
- Usuario: `admin`
- Contraseña: `admin123`

---

## Funcionalidad

- **Menú principal:** Inicio de sesión y registro seguro de usuarios.
- **CRUD de productos:** Solo usuarios autenticados acceden según sus roles (`admin`/`user`).
- **Registro de usuario:** La contraseña se guarda con hash bcrypt en `usuarios.json`.
- **Evidencia:** Imágenes, capturas y texto muestran login exitoso y fallido, y el hash guardado.

---

## Archivos importantes

- `src/main/resources/shiro.ini`: Configuración de usuarios y roles con Shiro.
- `src/main/resources/usuarios.json`: Guardado seguro de usuarios y hashes.
- `src/main/java/uptc/edu/co/App.java`: Aplicación principal (menú, seguridad y CRUD).
- `src/main/java/uptc/edu/co/service/UsuarioService.java` y `ProductoService.java`: Lógica de usuarios y productos.

---

## ¿Cómo evidenciar los entregables?

1. Captura la consola mostrando login exitoso y fallido (con mensaje de Shiro).
2. Muestra el archivo `usuarios.json` con el hash bcrypt generado.
3. Agrega pantallazos del funcionamiento del CRUD y del registro de usuarios.
4. Agrega breve explicación en este README de cómo has cumplido cada punto.

---

## Dependencias clave
- Apache Shiro
- JBCrypt (bcrypt)
- Gson

---

## Autoría y agradecimientos

Proyecto realizado para el Taller Final de Seguridad en Software II.

