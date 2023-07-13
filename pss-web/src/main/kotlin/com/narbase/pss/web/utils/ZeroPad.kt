package com.narbase.pss.web.utils

fun Int.zeroPad(): String {
    return if (this > 9) this.toString() else "0$this"
}