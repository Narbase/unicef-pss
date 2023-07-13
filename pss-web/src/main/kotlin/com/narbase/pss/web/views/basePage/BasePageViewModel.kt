package com.narbase.pss.web.views.basePage

import com.narbase.pss.web.utils.notifications.NotificationsController


class BasePageViewModel {

    fun onViewCreated() {
        NotificationsController.connect()
    }

    data class RouteDetails(var href: String, val title: String, val image: String? = null)

}
