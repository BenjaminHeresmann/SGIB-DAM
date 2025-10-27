package com.bomberos.sgib.util

// Clase Resource: Wrapper para manejo de estados de operaciones asincronas
// Es un patron de diseno ampliamente usado en Android para manejar estados de red y operaciones asincronas
// Permite representar tres estados posibles: Loading (cargando), Success (exito), Error (error)
// Inspirado en la arquitectura recomendada por Google para aplicaciones Android

// Sealed class: Clase sellada que restringe la jerarquia de herencia
// Solo puede tener subclases definidas en el mismo archivo
// Esto garantiza que Resource solo puede ser Success, Error o Loading
// El compilador puede verificar exhaustivamente todos los casos en expresiones when

// Tipo generico T: Permite que Resource funcione con cualquier tipo de dato
// Ejemplo: Resource<List<Bombero>>, Resource<User>, Resource<Stats>
sealed class Resource<T>(
    // Data: El dato resultante de la operacion
    // Es nullable (puede ser null) porque en estado Loading o Error puede no haber datos
    // En Success siempre tiene un valor, en Loading/Error puede tenerlo o no
    val data: T? = null,

    // Message: Mensaje descriptivo, generalmente usado para errores
    // Es nullable porque en Success o Loading puede no necesitarse un mensaje
    // En Error contiene la descripcion del error para mostrar al usuario
    val message: String? = null
) {
    // Subclase Success: Representa una operacion completada exitosamente
    // Contiene los datos resultantes de la operacion
    // Ejemplo: Resource.Success(listaDeBomberos)
    class Success<T>(data: T) : Resource<T>(data)

    // Subclase Error: Representa una operacion que fallo
    // Contiene un mensaje de error y opcionalmente datos parciales
    // El mensaje describe que salio mal para mostrar al usuario
    // data es opcional: puede haber datos en cache aunque la operacion falle
    // Ejemplo: Resource.Error("No se pudo conectar al servidor")
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // Subclase Loading: Representa una operacion en progreso
    // Opcionalmente puede contener datos anteriores (cache) mientras se cargan datos nuevos
    // Permite mostrar un spinner o indicador de carga en la UI
    // Ejemplo: Resource.Loading()
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

// Este Resource se conecta con:
// - Todos los ViewModels: usan Resource para exponer estados a la UI
// - Todos los Repositories: retornan Resource desde sus metodos
// - Todas las Screens: observan Resource para mostrar Loading, Success o Error
// Ejemplo de uso en ViewModel:
//   _uiState.value = Resource.Loading()
//   val result = repository.getBomberos()
//   _uiState.value = if (result.isSuccess) {
//       Resource.Success(result.data)
//   } else {
//       Resource.Error(result.message)
//   }
// Ejemplo de uso en Screen:
//   when (val state = viewModel.uiState) {
//       is Resource.Loading -> LoadingScreen()
//       is Resource.Success -> ShowData(state.data)
//       is Resource.Error -> ErrorScreen(state.message)
//   }


