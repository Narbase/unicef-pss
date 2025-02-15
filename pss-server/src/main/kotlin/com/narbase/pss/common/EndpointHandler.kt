/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


package com.narbase.pss.common


import com.narbase.pss.dto.models.RequestType
import com.narbase.pss.router.EndPoint
import kotlin.reflect.KClass


abstract class EndpointHandler<V : Any, out D : Any>(
    private val requestDtoClass: KClass<V>,
    val endPoint: EndPoint<V, D>,
) : Handler<V, D>(requestDtoClass)

abstract class BasicEndpointHandler<out D : Any>(
    val endPoint: EndPoint<RequestType.FormData, D>,
) : BasicHandler<D>()