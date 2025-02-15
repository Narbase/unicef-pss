package com.narbase.pss.web.utils.session

import com.narbase.pss.dto.models.roles.Privilege
import com.narbase.pss.web.storage.SessionInfo

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

inline fun authorized(privileges: List<Privilege> = listOf(), block: () -> Unit) {
    if (isAuthorized(privileges)) {
        block()
    }
}

fun isAuthorized(privileges: List<Privilege> = listOf()): Boolean {
    return SessionInfo.loggedInUser.privileges.any { it in privileges }
}
