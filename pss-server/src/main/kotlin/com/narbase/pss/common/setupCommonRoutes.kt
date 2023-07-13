/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


package com.narbase.pss.common

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.reflections.Reflections
import com.narbase.pss.common.auth.loggedin.AuthorizedClientData
import com.narbase.pss.common.exceptions.DisabledUserException
import com.narbase.pss.data.tables.UsersTable
import com.narbase.pss.deployment.Environment
import com.narbase.pss.deployment.LaunchConfig
import com.narbase.pss.domain.user.crud.*
import com.narbase.pss.domain.utils.addPrivilegeVerificationInterceptor
import com.narbase.pss.dto.models.roles.Privilege
import com.narbase.pss.router.CrudEndPoint
import com.narbase.pss.router.EndPoint
import com.narbase.pss.router.newSubEndPoint
import java.util.*
import java.util.logging.Logger
import io.ktor.server.routing.Route as KtorRoute
import io.ktor.server.routing.Routing as KtorRouting
import com.narbase.pss.router.Routing as CommonRouting


fun KtorRouting.setupCommonRoutes() {
    val logger = Logger.getLogger("setupCommonRoutes")
    CommonRouting.init()
    val commonRoutingEndPoints = CommonRouting.endPoints
    val registeredEndpointPathes = mutableSetOf<String>()

    val reflections = Reflections("com.narbase.pss")
    reflections.getSubTypesOf(EndpointHandler::class.java).forEach {
        logger.info("Registering controller: ${it.simpleName}")
        val defaultConstructors = it.constructors.first { it.parameterCount == 0 }
        val handler = defaultConstructors.newInstance() as EndpointHandler<*, *>
        registeredEndpointPathes.add(handler.endPoint.path)
        registerRoute(handler, handler.endPoint)
    }

    reflections.getSubTypesOf(BasicEndpointHandler::class.java).forEach {
        logger.info("Registering basic controller: ${it.simpleName}")
        val defaultConstructors = it.constructors.first { it.parameterCount == 0 }
        val handler = defaultConstructors.newInstance() as BasicEndpointHandler<*>
        registeredEndpointPathes.add(handler.endPoint.path)
        registerRoute(handler, handler.endPoint)
    }

    reflections.getSubTypesOf(EndpointCrudController::class.java).forEach {
        logger.info("Registering crud controller: ${it.simpleName}")
        val defaultConstructors = it.constructors.first { it.parameterCount == 0 }
        val crud = defaultConstructors.newInstance() as EndpointCrudController<Any, Any>
        registeredEndpointPathes.addAll(crud.endPoints.map { it.path })
        crud.endPoints.forEach {
            val endPoint = it

            if (CrudEndPoint.CrudEndpoints.AddEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    CreateController(crud::createItem, crud.dtoClass),
                    newSubEndPoint("/add", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.DetailsEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    GetItemController(crud::getItem),
                    newSubEndPoint("/details", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.UpdateEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    UpdateController(crud::updateItem, crud.dtoClass),
                    newSubEndPoint("/update", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.DeleteEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    DeleteController(crud::deleteItem),
                    newSubEndPoint("/delete", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.ListEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    GetListController(
                        crud.ListRequestDtoClass,
                        crud::getItemsList,
                        crud.defaultPageSize
                    ),
                    newSubEndPoint("/list", endPoint)
                )
            }

        }
    }

    val unregisterEndPoints = commonRoutingEndPoints.filter { it.path !in registeredEndpointPathes }
    if (unregisterEndPoints.isNotEmpty()) {
        System.err.println("Unregistered EndPoints:")
        unregisterEndPoints.forEach {
            System.err.println(it)
        }
        throw IllegalStateException("Some endpoints are unregistered!")
    }
    if (LaunchConfig.environment == Environment.Dev) {
        printRoutes()
    }


}


private fun KtorRouting.registerRoute(handler: BasicHandler<*>, endPoint: EndPoint<*, *>) {
    var authenticatedRoute: KtorRoute = this@registerRoute
    endPoint.authentication.forEach { auth ->
        authenticatedRoute.apply { authenticate(auth.configurationName) current@{ authenticatedRoute = this@current } }
    }
    authenticatedRoute.apply { registerRouteWithoutAuth(endPoint, handler) }

}

private fun KtorRoute.registerRoute(handler: BasicHandler<*>, endPoint: EndPoint<*, *>) {
    var authenticatedRoute: KtorRoute = this@registerRoute
    endPoint.authentication.forEach { auth ->
        authenticatedRoute.apply { authenticate(auth.configurationName) current@{ authenticatedRoute = this@current } }
    }
    authenticatedRoute.apply { registerRouteWithoutAuth(endPoint, handler) }

}

private fun KtorRoute.registerRouteWithoutAuth(
    endPoint: EndPoint<*, *>,
    handler: BasicHandler<*>,
) {
    if (endPoint.privileges.isEmpty()) {
        post(endPoint.path) { handler.handle(call) }
    } else {
        addPrivilegeVerificationInterceptor(endPoint.privileges)
        addDisableAccountInterceptor(endPoint.privileges)
        post(endPoint.path) {
            handler.handle(call)
        }
    }
}


private fun KtorRoute.addDisableAccountInterceptor(privileges: List<Privilege>) {
    intercept(ApplicationCallPipeline.Call) {
        val authorizedClientData = call.principal<AuthorizedClientData>()
        val isInactive = transaction {
            authorizedClientData?.id?.let { clientId ->
                            UsersTable.select { UsersTable.clientId eq UUID.fromString(clientId) }
                                .firstOrNull()
                                ?.let { it[UsersTable.isInactive] || it[UsersTable.isDeleted] }
                                ?: true
            }
        }
        if (isInactive == true)
            throw DisabledUserException()
    }
}

fun KtorRouting.printRoutes() {
    val allRoutes = allRoutes(this)
    val allRoutesWithMethod = allRoutes.filter { it.selector is HttpMethodRouteSelector }
    allRoutesWithMethod.forEach {
        println("route: $it")
    }
}

fun allRoutes(root: KtorRoute): List<KtorRoute> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
}