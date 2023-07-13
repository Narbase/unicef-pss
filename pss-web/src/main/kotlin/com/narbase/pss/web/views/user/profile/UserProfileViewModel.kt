package com.narbase.pss.web.views.user.profile

import com.narbase.kunafa.core.lifecycle.Observable
import com.narbase.pss.dto.domain.user.profile.GetProfileDto
import com.narbase.pss.web.network.ServerCaller
import com.narbase.pss.web.network.basicNetworkCall
import com.narbase.pss.web.utils.BasicUiState

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
class UserProfileViewModel {
    val getProfileUiState = Observable<BasicUiState>()
    val updateProfileUiState = Observable<BasicUiState>()

    var loadedProfile: GetProfileDto.UserProfile? = null

    fun getProfile() {

        basicNetworkCall(getProfileUiState) {
            val response = ServerCaller.getUserProfiles()
            loadedProfile = response.data.profile
        }
    }

    fun updateProfile(
        fullName: String,
        callingCode: String,
        localPhone: String
    ) {
        basicNetworkCall(updateProfileUiState) {
            val dto = UpdateUserProfileDto.RequestDto(fullName, callingCode, localPhone)
            ServerCaller.updateUserProfile(dto)
        }
    }

}
