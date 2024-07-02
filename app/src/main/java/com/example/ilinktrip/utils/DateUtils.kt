package com.example.ilinktrip.utils

import com.example.ilinktrip.application.GlobalConst
import org.threeten.bp.format.DateTimeFormatter

class DateUtils {
    private val UIFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(GlobalConst.DATE_UI_FORMATTER)

    fun getUIDateFormatter(): DateTimeFormatter {
        return this.UIFormatter
    }
}