package com.teoryul.mytesy.data.oldlogin

import kotlinx.serialization.Serializable

@Serializable
data class OldAppLoginResponse(
    val acc_alt: String,
    val acc_session: String
)