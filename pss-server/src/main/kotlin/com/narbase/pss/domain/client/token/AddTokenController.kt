package com.narbase.pss.domain.client.token

import com.google.gson.annotations.SerializedName
import com.narbase.pss.common.DataResponse
import com.narbase.pss.common.Handler
import com.narbase.pss.common.auth.loggedin.AuthorizedClientData
import com.narbase.pss.common.exceptions.UnauthenticatedException
import com.narbase.pss.data.tables.ClientsTable
import com.narbase.pss.data.tables.DeviceTokensTable
import com.narbase.pss.data.tables.utils.toEntityId
import com.narbase.pss.domain.client.token.AddTokenController.RequestDto
import com.narbase.pss.domain.client.token.AddTokenController.ResponseDto
import com.narbase.pss.domain.client.token.RemoveTokenController.Companion.removeClientToken
import com.narbase.pss.domain.utils.client
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class AddTokenController : Handler<RequestDto, ResponseDto>(RequestDto::class) {

    class RequestDto(
        @SerializedName("token")
        val token: String
    )

    class ResponseDto

    override fun process(requestDto: RequestDto, clientData: AuthorizedClientData?): DataResponse<ResponseDto> {
        val client = clientData?.client ?: throw UnauthenticatedException()
        transaction {
            removeClientToken(client, requestDto.token)
            DeviceTokensTable.insert {
                it[token] = requestDto.token
                it[clientId] = client.id.toEntityId(ClientsTable)
                it[createdOn] = DateTime()
            }
        }
        return DataResponse(ResponseDto())
    }
}