package com.example.todaysforecast.utils

import android.text.SpannedString
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import java.util.*


fun String.makeTitleBold(): SpannedString {
    val pattern = "\\d".toRegex()
    val matches = pattern.find(this)
    val indexed = matches?.value?.let { this.indexOf(it) }
    val boldStr = indexed?.minus(1)?.let { this.substring(0, it) }
    val normalStr = indexed?.let { this.substring(it, this.length) }
    return buildSpannedString {
        bold { append("$boldStr") }
        append(" $normalStr")
    }

}

fun Long.toReadableDate(): String {
    //Get instance of calendar
    val calendar = Calendar.getInstance(Locale.getDefault())
    //get current date from ts
    calendar.timeInMillis = this * 1000
    //return formatted date
    return android.text.format.DateFormat.format("dd-MM-yyyy hh:mm a", calendar).toString()
}