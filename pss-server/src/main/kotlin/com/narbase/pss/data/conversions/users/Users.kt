package com.narbase.pss.data.conversions.users

import com.narbase.pss.data.conversions.id.toDto
import com.narbase.pss.data.conversions.roles.toDto
import com.narbase.pss.data.models.users.UserRm
import com.narbase.pss.dto.domain.user.profile.GetProfileDto
import com.narbase.pss.dto.domain.usersmanagement.UsersCrudDto

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun UserRm.toCrudDto() = UsersCrudDto.User(
    client.id.toDto(),
    user.id.value.toDto(),
    client.username,
    "",
    user.fullName,
    user.callingCode,
    user.localPhone,
    roles.map { it.toDto() }.toTypedArray()
)

fun UserRm.toProfileDto() = GetProfileDto.UserProfile(
    client.id.toString(),
    user.id.value.toString(),
    user.fullName,
    client.username,
    user.callingCode,
    user.localPhone,
    roles.map { it.privileges.map { it.dtoName } }.flatten().toTypedArray()
)