package com.teoryul.mytesy.data.model.oldlogin

import kotlinx.serialization.Serializable

@Serializable
data class OldAppLoginResponse(
    val acc_alt: String? = null,
    val acc_session: String? = null,
    val error: String? = null
)