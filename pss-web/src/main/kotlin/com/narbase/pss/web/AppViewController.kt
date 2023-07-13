package com.narbase.pss.web

import com.narbase.kunafa.core.lifecycle.Observable
import com.narbase.pss.web.login.LoginPageNavigator
import com.narbase.pss.web.storage.StorageManager
import com.narbase.pss.web.views.basePage.HomePageNavigator


class AppViewController : LoginPageNavigator,
    HomePageNavigator {

    companion object {
        val loginState = Observable<Boolean>()
    }

    override fun onLoggedInSuccessful() {
        StorageManager.setUserLoggedIn(true)
        loginState.value = true
    }

    override fun onLogoutSelected() {
        StorageManager.setUserLoggedIn(false)
        loginState.value = false
    }

    fun onViewCreated() {
        loginState.value = StorageManager.isUserLoggedIn()
    }

}
