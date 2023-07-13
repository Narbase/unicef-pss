/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
package com.narbase.pss.domain.user.profile

import com.narbase.pss.common.DataResponse
import com.narbase.pss.common.EndpointHandler
import com.narbase.pss.common.auth.loggedin.AuthorizedClientData
import com.narbase.pss.dto.domain.hello_world.HelloWorldEndPoint


class HelloWorldController : EndpointHandler<HelloWorldEndPoint.Request, HelloWorldEndPoint.Response>(
    HelloWorldEndPoint.Request::class,
    HelloWorldEndPoint
) {
    override fun process(
        requestDto: HelloWorldEndPoint.Request,
        clientData: AuthorizedClientData?,
    ): DataResponse<HelloWorldEndPoint.Response> {
        return DataResponse(HelloWorldEndPoint.Response("${requestDto.data}:Hehe"))
    }


}
