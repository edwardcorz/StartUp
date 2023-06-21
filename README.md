## Thruster App

Esta es una aplicación móvil desarrollada en Kotlin que permite a los usuarios gestionar su membresía en un gimnasio. Los usuarios podrán realizar las siguientes acciones:

- Ver el resumen de los días en los que han asistido al gimnasio.
- Realizar pagos o aumentar su membresía desde la aplicación.
- Consultar los horarios y los ejercicios del día.
- Configurar su foto de perfil.
- Cambiar su contraseña.

## Características principales

- **Resumen de asistencia:** Los usuarios podrán ver un resumen de los días en los que han asistido al gimnasio, lo que les permitirá realizar un seguimiento de su progreso y motivarse.
- **Gestión de membresía:** Los usuarios podrán realizar pagos desde la aplicación para renovar o aumentar su membresía en el gimnasio.
- **Horarios y ejercicios diarios:** Los usuarios podrán consultar los horarios y los ejercicios programados para el día, lo que les permitirá planificar sus rutinas de entrenamiento.
- **Configuración de perfil:** Los usuarios podrán configurar su foto de perfil para personalizar su cuenta.
- **Cambio de contraseña:** Los usuarios podrán cambiar su contraseña de forma segura desde la aplicación.

## Requisitos de instalación

Para ejecutar la aplicación en tu dispositivo, sigue los siguientes pasos:

1. Clona este repositorio en tu máquina local:

   ```
   git clone https://github.com/edwardcorz/StartUp.git
   ```

2. Abre el proyecto en Android Studio.

3. Configura tu dispositivo móvil o un emulador para ejecutar la aplicación.

4. Ejecuta la aplicación en tu dispositivo o emulador.

## Dependencias

La aplicación utiliza las siguientes dependencias de terceros:

- `MPAndroidChart` - Para las implementacion de las graficas
- `Cardview` - Para manejo y gestion de las cardview.
- `Firebase Storege` - Para cargar y mostrar imágenes de perfil de usuario.
- `Firebase Authentication` - Para autenticación y cambio de contraseña.

Todas las dependencias están especificadas en el archivo `build.gradle`.

## Estructura del proyecto

La estructura del proyecto se organiza de la siguiente manera:

- `app/src/main/java/com/startup`: Contiene los archivos fuente de la aplicación.
  - `activities`: Contiene las actividades principales de la aplicación.
  - `fragments`: Contiene los fragmentos utilizados en las actividades.
- `app/src/main/res`: Contiene los recursos de la aplicación, como diseños XML, imágenes y archivos de traducción.

## Licencia

Este proyecto está bajo la Licencia MIT. Puedes consultar el archivo `LICENSE` para más información.
