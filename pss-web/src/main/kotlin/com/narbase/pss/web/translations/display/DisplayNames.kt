package com.narbase.pss.web.translations.display

import com.narbase.pss.dto.models.roles.Privilege
import com.narbase.pss.web.utils.string.splitCamelCase

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

val Privilege.displayName: String
    get() {
        return when (this) {
            Privilege.BasicUser -> "Basic User"
            Privilege.UsersManagement -> "Users Management"
            else -> name.splitCamelCase()
        }
    }