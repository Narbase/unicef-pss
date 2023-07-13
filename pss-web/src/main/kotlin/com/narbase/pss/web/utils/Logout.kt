package com.narbase.pss.web.utils

import com.narbase.pss.web.AppViewController
import com.narbase.pss.web.storage.StorageManager

fun logoutUser() {
    StorageManager.accessToken = null
    StorageManager.setUserLoggedIn(false)
    AppViewController.loginState.value = false
}
