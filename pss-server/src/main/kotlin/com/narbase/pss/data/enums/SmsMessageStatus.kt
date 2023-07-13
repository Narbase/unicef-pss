package com.narbase.pss.data.enums

import com.narbase.pss.data.columntypes.EnumPersistenceName
import com.narbase.pss.dto.common.EnumDtoName

enum class SmsMessageStatus(override val persistenceName: String, override val dtoName: String) : EnumPersistenceName,
    EnumDtoName {
    /**
     * Message not send and its timeToSend has not passed
     */
    Pending("Pending", "Pending"),

    Sent("Sent", "Sent"),

    Failed("Failed", "Failed"),
}