package com.narbase.pss.main.provisioning

import com.narbase.pss.data.access.clients.ClientsDao
import com.narbase.pss.data.access.roles.RolesDao
import com.narbase.pss.data.access.users.UsersRepository
import com.narbase.pss.deployment.FirstRunConfig
import com.narbase.pss.dto.models.roles.Privilege
import org.jetbrains.exposed.sql.transactions.transaction

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun registerFirstAdmin() {
    val isClientsTableEmpty = transaction { ClientsDao.getClients(0, 1).isEmpty() }
    if (isClientsTableEmpty.not()) return
    val roleId = transaction { RolesDao.create("Super admin", Privilege.values().toList()) }
    UsersRepository.create(
        FirstRunConfig.adminUsername,
        FirstRunConfig.adminPassword,
        "First run admin",
        "249",
        "9999999999",
        listOf(roleId)
    )
}