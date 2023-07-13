package com.narbase.pss.data.dto.roles

import com.narbase.pss.dto.models.roles.Privilege
import com.narbase.pss.web.common.data.roles.DynamicRole
import com.narbase.pss.web.network.valueOfDto

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

typealias PrivilegeName = String

data class DynamicRoleDto(
    val id: String?,
    val name: String,
    val privileges: Array<PrivilegeName>,
    val isDoctor: Boolean
) {

    constructor(role: DynamicRole) : this(
        role.id,
        role.name,
        role.privileges.map { it.dtoName }.toTypedArray(),
        role.isDoctor
    )

}

val DynamicRoleDto.privilegesEnums
    get() = privileges.mapNotNull { valueOfDto<Privilege>(it) }
