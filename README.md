# SGIB DAM - Sistema de Gestión Integral de Bomberos

## 📱 Aplicación Android para la Segunda Compañía de Bomberos de Viña del Mar

### 🚀 Descripción

Sistema integral de gestión para bomberos desarrollado en **Kotlin** con **Jetpack Compose** y arquitectura **Clean Architecture** + **MVVM**.

### ✨ Características Implementadas

#### 🔐 Autenticación
- Login con validación
- Gestión de sesión con DataStore
- Autenticación biométrica (huella digital)

#### 👥 Gestión de Bomberos
- ✅ Lista completa de bomberos con búsqueda y filtros
- ✅ Detalle de bombero con información completa
- ✅ Formulario de creación y edición
- ✅ Selector de foto desde galería o cámara
- ✅ Validación de campos (RUT, email, teléfono)
- ✅ Estadísticas generales en Dashboard

#### 📅 Módulo de Citaciones (NUEVO)
- ✅ Lista de citaciones con diseño Material 3
- ✅ Filtros por estado (Pendiente, Confirmada, En Curso, Completada, Cancelada)
- ✅ Filtros por tipo de actividad (Entrenamiento, Guardia, Reunión, Ceremonia, Ejercicio)
- ✅ Badges de estado con colores
- ✅ Iconos específicos por tipo de actividad
- ✅ Indicador de progreso de asistencia
- ✅ Navegación desde Dashboard
- ✅ 5 citaciones de ejemplo con datos realistas

### 🏗️ Arquitectura

```
app/
├── data/
│   ├── local/          # DataStore, PreferencesManager
│   ├── remote/         # ApiService, DTOs, Interceptors
│   └── repository/     # Implementación de repositorios
├── domain/
│   └── model/          # Modelos de dominio (Bombero, Citacion, Stats, User)
├── di/                 # Inyección de dependencias (Hilt)
└── ui/
    ├── components/     # Componentes reutilizables
    ├── navigation/     # NavGraph, Screen
    ├── screens/        # Pantallas de la app
    │   ├── bomberos/
    │   ├── citaciones/  # NUEVO
    │   ├── dashboard/
    │   ├── detalle/
    │   ├── form/
    │   └── login/
    └── theme/          # Material 3 Theme
```

### 🛠️ Tecnologías

- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI moderna y declarativa
- **Material 3** - Design System
- **Hilt** - Inyección de dependencias
- **Navigation Compose** - Navegación entre pantallas
- **Coil** - Carga de imágenes
- **DataStore** - Persistencia de preferencias
- **Retrofit** - Cliente HTTP (preparado para backend)
- **Accompanist** - Librerías complementarias

### 📦 Requisitos

- Android Studio Hedgehog | 2023.1.1 o superior
- Kotlin 1.9.0 o superior
- Gradle 8.2 o superior
- SDK mínimo: API 26 (Android 8.0)
- SDK objetivo: API 34 (Android 14)

### 🚀 Instalación

1. **Clonar el repositorio:**
```bash
git clone https://github.com/BenjaminHeresmann/SGIB-DAM.git
cd SGIB-DAM/android-app
```

2. **Abrir en Android Studio:**
   - File → Open → Seleccionar carpeta `android-app`
   - Esperar a que Gradle sincronice

3. **Ejecutar:**
   - Conectar dispositivo Android o iniciar emulador
   - Click en Run ▶️

### 🔑 Credenciales de Prueba

```
Usuario: admin
Contraseña: admin123
```

### 📱 Pantallas

1. **Login** - Autenticación con usuario/contraseña o biometría
2. **Dashboard** - Vista general con estadísticas y accesos rápidos
3. **Lista de Bomberos** - Tabla con búsqueda, filtros y paginación
4. **Detalle de Bombero** - Información completa de cada bombero
5. **Formulario** - Crear/editar bomberos con validación
6. **Citaciones** - Lista de actividades programadas (NUEVO ✨)

