package com.teoryul.mytesy.ui.common

import android.content.Context
import android.widget.Toast
import org.koin.core.context.GlobalContext

actual fun showToast(message: String) {
    val ctx: Context = GlobalContext.get().get()
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}