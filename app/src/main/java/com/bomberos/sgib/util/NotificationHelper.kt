package com.bomberos.sgib.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bomberos.sgib.MainActivity
import com.bomberos.sgib.R
import com.bomberos.sgib.domain.model.Citacion
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Helper para gestionar notificaciones
 * Recurso Nativo 3: Sistema de notificaciones push locales
 */
@SuppressLint("MissingPermission")
class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "citaciones_channel"
        const val CHANNEL_NAME = "Citaciones"
        const val CHANNEL_DESCRIPTION = "Notificaciones de citaciones próximas"
        const val NOTIFICATION_ID_BASE = 1000
    }

    init {
        createNotificationChannel()
    }

    /**
     * Crear canal de notificaciones (requerido para Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                importance
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Mostrar notificación de citación próxima
     */
    fun showCitacionNotification(citacion: Citacion) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("citacion_id", citacion.id)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            citacion.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Citación: ${citacion.titulo}")
            .setContentText(getNotificationText(citacion))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getNotificationBigText(citacion)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_BASE + citacion.id, notification)
        }
    }

    /**
     * Texto breve de la notificación
     */
    private fun getNotificationText(citacion: Citacion): String {
        val horasRestantes = ChronoUnit.HOURS.between(LocalDateTime.now(), citacion.fecha)
        return when {
            horasRestantes < 1 -> "¡Comienza en menos de 1 hora!"
            horasRestantes < 24 -> "Comienza en $horasRestantes horas"
            else -> {
                val diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), citacion.fecha)
                "Comienza en $diasRestantes días"
            }
        }
    }

    /**
     * Texto extendido de la notificación
     */
    private fun getNotificationBigText(citacion: Citacion): String {
        return buildString {
            append("📍 ${citacion.lugar}\n")
            append("🕐 ${citacion.fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}\n")
            append("👥 ${citacion.asistentesConfirmados}/${citacion.asistentesRequeridos} confirmados\n")
            if (citacion.observaciones != null) {
                append("\n${citacion.observaciones}")
            }
        }
    }

    /**
     * Notificación de recordatorio (1 día antes)
     */
    fun showRecordatorioDiaAntes(citacion: Citacion) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Recordatorio: ${citacion.titulo}")
            .setContentText("Mañana tienes esta citación")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("📅 Mañana ${citacion.fecha.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}\n📍 ${citacion.lugar}"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_BASE + 1000 + citacion.id, notification)
        }
    }

    /**
     * Notificación de recordatorio (1 hora antes)
     */
    fun showRecordatorioHoraAntes(citacion: Citacion) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("⚠️ Citación en 1 hora")
            .setContentText(citacion.titulo)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("${citacion.titulo}\n\n📍 ${citacion.lugar}\n🕐 En 1 hora"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .build()

        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_BASE + 2000 + citacion.id, notification)
        }
    }

    /**
     * Notificación de confirmación de asistencia
     */
    fun showConfirmacionAsistencia(citacion: Citacion) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("✅ Asistencia Confirmada")
            .setContentText(citacion.titulo)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Has confirmado tu asistencia a:\n${citacion.titulo}\n\n📅 ${citacion.fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}\n📍 ${citacion.lugar}"))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_BASE + 3000 + citacion.id, notification)
        }
    }

    /**
     * Notificación de nueva citación
     */
    fun showNuevaCitacion(citacion: Citacion) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🆕 Nueva Citación")
            .setContentText(citacion.titulo)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("${citacion.titulo}\n\n📅 ${citacion.fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}\n📍 ${citacion.lugar}\n\n${citacion.descripcion}"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID_BASE + 4000 + citacion.id, notification)
        }
    }

    /**
     * Cancelar notificación específica
     */
    fun cancelNotification(citacionId: Int) {
        NotificationManagerCompat.from(context)
            .cancel(NOTIFICATION_ID_BASE + citacionId)
    }

    /**
     * Cancelar todas las notificaciones
     */
    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context)
            .cancelAll()
    }

    /**
     * Verificar si tenemos permiso de notificaciones
     */
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } else {
            true
        }
    }

    /**
     * Verificar si las notificaciones están habilitadas
     */
    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}

