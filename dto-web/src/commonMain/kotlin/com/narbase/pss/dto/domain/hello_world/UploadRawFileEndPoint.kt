/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
package com.narbase.pss.dto.domain.hello_world

import com.narbase.pss.dto.models.RequestType
import com.narbase.pss.router.EndPoint

object UploadRawFileEndPoint : EndPoint<RequestType.FormData, UploadRawFileEndPoint.Response>() {

    class Response(
        val file: FileDto,
    )
}

object UploadFileEndPoint : EndPoint<RequestType.FormData, UploadFileEndPoint.Response>() {

    class Response(
        val file: FileDto,
    )
}

class FileDto(
    val url: String,
    val fileName: String,
)