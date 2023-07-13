package com.narbase.pss.domain.user.login

import com.narbase.pss.common.DataResponse
import com.narbase.pss.common.Handler
import com.narbase.pss.common.auth.loggedin.AuthorizedClientData
import com.narbase.pss.data.access.clients.ClientsDao
import com.narbase.pss.domain.utils.authenticatedClient
import com.narbase.pss.dto.domain.user.login.LoginDto
import org.jetbrains.exposed.sql.transactions.transaction

class LoginController : Handler<LoginDto.Request, LoginDto.Response>(LoginDto.Request::class) {

    override fun process(
        requestDto: LoginDto.Request,
        clientData: AuthorizedClientData?
    ): DataResponse<LoginDto.Response> {
        val client = clientData.authenticatedClient

        val clientLastLogin = client.lastLogin
        transaction { ClientsDao.updateLastLogin(client.id) }

        return DataResponse(LoginDto.Response(clientLastLogin == null))
    }

}