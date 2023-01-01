package com.temp.ggtest.util

object Util {
    fun String.readFile(c: Any) : String =
        c.javaClass.classLoader!!.getResource(this).readText()
}