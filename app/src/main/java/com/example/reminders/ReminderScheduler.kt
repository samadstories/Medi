package com.example.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.data.model.MedicineReminder
import java.util.Calendar

object ReminderScheduler {

    fun syncReminders(context: Context, reminders: List<MedicineReminder>) {
        ReminderReceiver.ensureChannel(context)
        reminders.forEach { reminder ->
            if (reminder.active) {
                schedule(context, reminder)
            } else {
                cancel(context, reminder.id)
            }
        }
    }

    fun schedule(context: Context, reminder: MedicineReminder) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode = reminder.id.hashCode()
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_MEDICINE_NAME, reminder.medicineName)
            putExtra(ReminderReceiver.EXTRA_DOSAGE, reminder.dosage)
            putExtra(ReminderReceiver.EXTRA_INSTRUCTIONS, reminder.instructions)
            putExtra(ReminderReceiver.EXTRA_REMINDER_HASH, requestCode)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.timeHour.toInt().coerceIn(0, 23))
            set(Calendar.MINUTE, reminder.timeMinute.toInt().coerceIn(0, 59))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        runCatching {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun cancel(context: Context, reminderId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
