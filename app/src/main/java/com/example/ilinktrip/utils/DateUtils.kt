package com.example.ilinktrip.utils

import com.example.ilinktrip.application.GlobalConst
import org.threeten.bp.format.DateTimeFormatter

class DateUtils {
    private val dataFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(GlobalConst.DATE_DATA_FORMATTER)
    private val UIFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(GlobalConst.DATE_UI_FORMATTER)

    fun getDataDateFormatter(): DateTimeFormatter {
        return this.dataFormatter
    }

    fun getUIDateFormatter(): DateTimeFormatter {
        return this.UIFormatter
    }
}