package com.narbase.pss.web.views.startup

import com.narbase.kunafa.core.lifecycle.Observable
import com.narbase.pss.dto.domain.user.profile.GetProfileDto
import com.narbase.pss.dto.models.roles.Privilege
import com.narbase.pss.web.network.ServerCaller
import com.narbase.pss.web.network.basicNetworkCall
import com.narbase.pss.web.storage.CurrentUserProfile
import com.narbase.pss.web.storage.SessionInfo
import com.narbase.pss.web.storage.StorageManager
import com.narbase.pss.web.utils.BasicUiState

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

class StartupViewModel {
    val getConfigUiState = Observable<BasicUiState>()


    fun getConfig() {
        if (StorageManager.isUserLoggedIn().not()) {
            SessionInfo.currentUser = null
            getConfigUiState.value = BasicUiState.Loaded
            return
        }
        basicNetworkCall(getConfigUiState) {
            val profile = ServerCaller.getUserProfiles().data.profile
            SessionInfo.currentUser = profile.toCurrentUserProfile()
            // You can check app version or subscription here as well
        }
    }

    private fun GetProfileDto.UserProfile.toCurrentUserProfile() =
        CurrentUserProfile(
            clientId,
            userId,
            fullName,
            username,
            callingCode,
            localPhone,
            privileges.map { Privilege.valueOf(it) })
}
