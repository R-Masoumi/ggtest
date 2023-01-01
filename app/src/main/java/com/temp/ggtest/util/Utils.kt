package com.temp.ggtest.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    /**
     * Provide custom date formatter
     */
    fun getDateFormat(): DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd LLL yyyy HH:mm:ss")
}