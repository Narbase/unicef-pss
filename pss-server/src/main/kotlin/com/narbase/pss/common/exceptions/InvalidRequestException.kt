package com.narbase.pss.common.exceptions

import com.narbase.pss.common.BasicResponse
import com.narbase.pss.common.CommonCodes

open class InvalidRequestException(message: String = "") : Exception(message)

class MissingArgumentException(parameter: String = "") : InvalidRequestException("parameter '$parameter' is missing")

class OutdatedAppException : Exception()

class InvalidRequestResponse(message: String) : BasicResponse(CommonCodes.INVALID_REQUEST, message)
