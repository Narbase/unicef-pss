/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

package com.narbase.pss.router

import com.narbase.pss.dto.domain.hello_world.*
import com.narbase.pss.dto.models.roles.Privilege


object Routing {
    val endPoints = mutableListOf<EndPoint<*, *>>()
    fun init() = api {
        "api" {
            "v1" {
                "hello_world" {
                    route(HelloWorldEndPoint)
                }

            }
        }
    }

    private fun api(block: Context.() -> Unit) {
        endPoints.clear()
        block(Context("", mutableSetOf(), mutableSetOf()))
    }

    private fun <V : Any, D : Any> Context.route(endPoint: EndPoint<V, D>) {
        endPoint.applyContext(this)
        addOrThrow(endPoint)
    }


    private fun <V : Any, D : Any> Context.crud(
        endPoint: CrudEndPoint<V, D>,
        configuration: CrudEndPoint.CrudEndpointsBuilder.() -> Unit = { enableExcept() },
    ) {
        val builder = CrudEndPoint.CrudEndpointsBuilder()
        builder.configuration()
        endPoint.applyContext(this, builder.endPointList)
        addOrThrow(endPoint)
    }

    private fun addOrThrow(endPoint: EndPoint<*, *>) {
        if (endPoint in endPoints)
            throw IllegalStateException("Endpoint $endPoint is already registered")
        endPoints.add(endPoint)
    }


    data class Context(
        val route: String,
        val privileges: MutableSet<Privilege>,
        val authentications: MutableSet<Authentication>,
    ) {
        fun require(vararg privilege: Privilege) {
            privileges.addAll(privilege)
            authentications.add(Authentication.JWT)
        }

        fun requireAny() = auth(Authentication.JWT)

        fun auth(vararg auth: Authentication) {
            authentications.addAll(auth)
        }

        operator fun String.invoke(block: Context.() -> Unit) {
            val upperPath = this
            val upperContext = this@Context
            block(
                Context(
                    "${upperContext.route}/$upperPath",
                    upperContext.privileges.toMutableSet(),
                    upperContext.authentications.toMutableSet()
                )
            )
        }

    }
}