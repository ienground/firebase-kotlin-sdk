package zone.ien.firebase.example.ui.toast

import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_after
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

public actual fun showToast(message: String) {
    dispatch_async(dispatch_get_main_queue()) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return@dispatch_async
        val alert = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )
        rootViewController.presentViewController(alert, animated = true, completion = null)

        // Automatically dismiss alert after 1.5 seconds to emulate android toast behavior
        val delayNanos = 1_500_000_000L
        dispatch_after(
            dispatch_time(DISPATCH_TIME_NOW, delayNanos),
            dispatch_get_main_queue()
        ) {
            alert.dismissViewControllerAnimated(true, completion = null)
        }
    }
}