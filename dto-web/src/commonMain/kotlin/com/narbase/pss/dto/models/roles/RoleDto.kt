package com.narbase.pss.dto.models.roles

import com.narbase.pss.dto.common.IdDto

typealias PrivilegeName = String

data class RoleDto(
    val id: IdDto?,
    val name: String,
    val privileges: List<PrivilegeName>,
)