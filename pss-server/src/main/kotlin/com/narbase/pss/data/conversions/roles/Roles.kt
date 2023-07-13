package com.narbase.pss.data.conversions.roles

import com.narbase.pss.core.valueOfDto
import com.narbase.pss.data.conversions.id.toDto
import com.narbase.pss.data.models.roles.Role
import com.narbase.pss.dto.models.roles.Privilege
import com.narbase.pss.dto.models.roles.RoleDto

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun Role.toDto() = RoleDto(id.toDto(), name, privileges.map { it.dtoName })

val RoleDto.privilegesEnums get() = privileges.mapNotNull { valueOfDto<Privilege>(it) }

