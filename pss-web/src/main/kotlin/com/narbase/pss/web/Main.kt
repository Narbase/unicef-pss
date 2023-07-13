package com.narbase.pss.web

import com.narbase.kunafa.core.components.Page
import com.narbase.kunafa.core.components.View
import com.narbase.kunafa.core.components.page
import com.narbase.kunafa.core.components.view
import com.narbase.kunafa.core.css.height
import com.narbase.kunafa.core.css.overflow
import com.narbase.kunafa.core.css.width
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.dimensions.vh
import com.narbase.kunafa.core.dimensions.vw
import com.narbase.pss.dto.domain.hello_world.HelloWorldEndPoint
import com.narbase.pss.router.Routing
import com.narbase.pss.web.common.models.Direction
import com.narbase.pss.web.events.EscapeClickedEvent
import com.narbase.pss.web.login.LoginPageContent
import com.narbase.pss.web.login.LoginViewController
import com.narbase.pss.web.network.networkCall
import com.narbase.pss.web.network.remoteProcess
import com.narbase.pss.web.storage.StorageManager
import com.narbase.pss.web.utils.eventbus.EventBus
import com.narbase.pss.web.utils.views.PopUpDialog
import com.narbase.pss.web.utils.views.SnackBar
import com.narbase.pss.web.utils.views.customViews.initWarningPopupDialog
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.KeyboardEvent

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


fun main() {
    Routing.init()
    requireAssetsForWebpack()
    Main().setup()

}



class Main {

    fun setup() {

        page {
            Page.title = "Narcore"
            Page.useRtl = (StorageManager.language.direction == Direction.RTL)

            style {
                overflow = "hidden"
                width = 100.vw
                height = 100.vh
            }

            PopUpDialog.popUpRootView = view {
                style {
                    width = 0.px
                    height = 0.px
                }
            }

            initWarningPopupDialog()
            setupEventListeners()
            SnackBar.setup()
            mountApp()
        }
    }

    private fun setupEventListeners() {
        document.addEventListener("onkeydown", { evt ->
            evt as KeyboardEvent
            val isEscape = if (evt.key != undefined) {
                (evt.key == "Escape" || evt.key == "Esc")
            } else (evt.keyCode == 27)

            if (isEscape) {
                EventBus.publish(EscapeClickedEvent())
            }
            Unit
        })
    }

    private fun View.mountApp() {
        val appViewController = AppViewController()
        val loginPage = LoginPageContent(LoginViewController(appViewController))

        mount(AppComponent(appViewController, loginPage))

    }
}



private fun requireAssetsForWebpack() {
    js("require('material-design-icons-iconfont/dist/material-design-icons.css')")
    js("require('typeface-open-sans')")
    js("require('tippy.js/index.css')")
    js("require('tippy.js/themes/light-border.css')")
    js("require('pikaday/css/pikaday.css')")
    js("require('flatpickr/dist/flatpickr.min.css')")

}
