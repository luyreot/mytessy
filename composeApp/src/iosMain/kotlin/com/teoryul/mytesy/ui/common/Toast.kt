package com.teoryul.mytesy.ui.common

import platform.Foundation.NSTimer
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

actual fun showToast(message: String) {
    val alert = UIAlertController.alertControllerWithTitle(
        title = null,
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )

    val root = UIApplication.sharedApplication.keyWindow?.rootViewController
    root?.presentViewController(alert, animated = true, completion = null)

    NSTimer.scheduledTimerWithTimeInterval(1.2, repeats = false) {
        alert.dismissViewControllerAnimated(true, completion = null)
    }
}