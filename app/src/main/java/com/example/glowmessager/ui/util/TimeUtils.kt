package com.example.glowmessager.ui.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeUtils {

    fun formatConversationTime(timestamp: Long): String {
        if (timestamp <= 0) return ""
        val now = Calendar.getInstance()
        val messageTime = Calendar.getInstance().apply { timeInMillis = timestamp }

        return when {
            isSameDay(now, messageTime) -> {
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
            }
            isYesterday(now, messageTime) -> {
                "Yesterday"
            }
            isSameYear(now, messageTime) -> {
                SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
            }
            else -> {
                SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(Date(timestamp))
            }
        }
    }

    fun formatMessageTime(timestamp: Long): String {
        if (timestamp <= 0) return ""
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(cal1: Calendar, cal2: Calendar): Boolean {
        val cal1Copy = cal1.clone() as Calendar
        cal1Copy.add(Calendar.DAY_OF_YEAR, -1)
        return isSameDay(cal1Copy, cal2)
    }

    private fun isSameYear(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }
}
