# Ut5.2ActEvaJmRiv

* **Descripción:**
Aplicación de escritorio desarrollada en Java para la gestión de notas personales. Este proyecto implementa una interfaz gráfica completa con persistencia de datos y sistema de autenticación de usuarios, cumpliendo con los requisitos de la actividad de evaluación UT5.2 (Nivel 1, Nivel 2 y Mejoras Avanzadas).

* **Características:**
    * Nivel 1: El Gestor de Notas (Lo básico)
        * Crear, editar y borrar: Puedes gestionar tus notas de forma súper intuitiva.
        * Todo a la vista: Al hacer clic en una nota de la lista, su contenido aparece al instante en la pantalla principal.
        * Avisos anti-errores: La aplicación te avisa si te olvidas de poner un título o si intentas hacer algo raro.
        * Botón del pánico (Borrar todo): Puedes vaciar tu lista de notas con un solo clic, pero no te preocupes, la app te pedirá confirmación antes para que no borres nada por accidente

    * Nivel 2: Cuentas de usuario y guardado (Persistencia)
        * Cada usuario tiene su cuenta: Puedes registrarte e iniciar sesión. Cada persona tiene su propio espacio y nadie puede ver las notas de los demás.
        * Contraseñas seguras: Para proteger tu cuenta, las contraseñas se encriptan (usando un sistema llamado SHA-256) antes de guardarse.
        * No pierdes nada: Al cerrar la aplicación, todas tus notas se guardan de forma automática en tu disco duro (en un archivo usuarios.dat). Cuando vuelvas a entrar, ahí seguirán.

    * Mejoras Avanzadas (Detalles extra para usarla mejor)
        * Buscador mágico (Live Search): Solo con empezar a escribir en el buscador, la lista de notas se filtra al instante, sin necesidad de pulsar enter ni buscar botones.
        * Guardado automático: Olvídate de darle a "Guardar" a cada rato. La app guarda tus cambios de forma automática mientras trabajas.
        * Exportar a TXT: Si quieres llevarte tus notas a otro sitio, puedes descargarlas todas juntas en un archivo de texto normal y corriente.
        * Interfaz inteligente: Los botones cambian de nombre o se desactivan solos dependiendo de lo que estés haciendo en ese momento, para que siempre sepas qué opciones tienes.

* **Cómo ejecutar el proyecto:**
    * Clona este repositorio en tu máquina local.
    * Abre el proyecto en tu IDE favorito (Eclipse, IntelliJ IDEA, NetBeans o VSCode).
    * Ejecuta el archivo app/Main.java.

* **Estructura del proyecto:**

Ut5.2ActEvaJmRiv/
├── app/
│   └── Main.java 
├── model/
│   ├── Nota.java
│   └── Usuario.java
├── util/
│   └── GestionDatos.java
└── view/
    ├── GestorNotas.java
    └── Login.java