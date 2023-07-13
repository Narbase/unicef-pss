package com.narbase.pss.domain.user.profile

import com.narbase.pss.common.DataResponse
import com.narbase.pss.common.Handler
import com.narbase.pss.common.auth.loggedin.AuthorizedClientData
import com.narbase.pss.common.exceptions.UnauthenticatedException
import com.narbase.pss.data.access.users.UsersRepository
import com.narbase.pss.data.conversions.users.toProfileDto
import com.narbase.pss.dto.domain.user.profile.GetProfileDto
import java.util.*

class GetProfileController : Handler<GetProfileDto.Request, GetProfileDto.Response>(GetProfileDto.Request::class) {

    override fun process(requestDto: GetProfileDto.Request, clientData: AuthorizedClientData?)
            : DataResponse<GetProfileDto.Response> {

        val clientId = UUID.fromString(clientData?.id ?: throw UnauthenticatedException())
        val userRm = UsersRepository.get(clientId)
        return DataResponse(GetProfileDto.Response(userRm.toProfileDto()))
    }
}