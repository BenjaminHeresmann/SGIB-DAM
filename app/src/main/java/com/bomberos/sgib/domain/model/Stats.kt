package com.bomberos.sgib.domain.model

// Modelo de dominio para Estadisticas del Dashboard
// Contiene metricas y datos agregados sobre los bomberos de la compania
// Se muestra en la pantalla principal (Dashboard) en forma de tarjetas informativas
// Permite visualizar rapidamente el estado de la compania

data class Stats(
    // Numero total de bomberos con estado "Activo"
    // Bomberos que estan actualmente en servicio regular
    // Se usa para mostrar en tarjeta "Bomberos Activos"
    val totalActivos: Int,

    // Numero total de bomberos con estado "Inactivo" o "Licencia"
    // Bomberos que no estan actualmente disponibles
    // Se usa para mostrar en tarjeta "Bomberos Inactivos"
    val totalInactivos: Int,

    // Numero total de bomberos registrados en el sistema
    // Suma de activos e inactivos (totalActivos + totalInactivos)
    // Se usa para mostrar en tarjeta "Total de Bomberos"
    val total: Int,

    // Lista de conteos de bomberos agrupados por rango jerarquico
    // Cada elemento contiene un rango y cuantos bomberos tienen ese rango
    // Ejemplo: [RangoCount("Comandante", 2), RangoCount("Capitan", 5), ...]
    // Se usa para mostrar distribucion jerarquica en el dashboard
    val porRango: List<RangoCount>,

    // Numero de bomberos que se registraron en el ultimo mes
    // Calcula bomberos cuya fechaIngreso esta dentro de los ultimos 30 dias
    // Se usa para mostrar crecimiento reciente de la compania
    val nuevosUltimoMes: Int
)

// Modelo auxiliar para contar bomberos por rango
// Representa un par de valores: un rango y su cantidad asociada
data class RangoCount(
    // Nombre del rango jerarquico
    // Ejemplos: "Voluntario", "Bombero", "Cabo", "Sargento", "Teniente", "Capitan", "Comandante"
    val rango: String,

    // Cantidad de bomberos que tienen este rango
    // Tipo Int: numero entero mayor o igual a 0
    val cantidad: Int
)
// RangoCount se usa en la lista porRango de Stats
// Permite crear graficos o listas de distribucion de rangos
// Ejemplo de uso en UI: "Comandante: 2, Capitan: 5, Teniente: 8"

// Este modelo se conecta con:
// - DashboardScreen: muestra todas las estadisticas en tarjetas
// - DashboardViewModel: calcula las estadisticas desde los datos de bomberos
// - BomberoRepository: proporciona los datos de bomberos para calcular estadisticas
// - FakeDataSource: datos fuente para el calculo de estadisticas


