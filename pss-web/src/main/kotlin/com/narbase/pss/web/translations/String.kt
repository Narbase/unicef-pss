package com.narbase.pss.web.translations

import com.narbase.pss.web.common.models.Language
import com.narbase.pss.web.storage.StorageManager

fun String.localized(vararg params: String): String {
    val key = this.toLowerCase()

    val getTranslationOrNull = when (StorageManager.language) {
        Language.Ar -> arabicMap[key]
        Language.En -> this
    }
    var translation = getTranslationOrNull ?: this

    if (getTranslationOrNull == null) {
        console.log("Missing ${StorageManager.language} translation")
        console.log(""""$this" to "",""")
    }

    params.forEach {
        translation = translation.replaceFirst("%s", it)
    }
    return translation
}
