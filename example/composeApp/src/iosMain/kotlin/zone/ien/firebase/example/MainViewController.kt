package zone.ien.firebase.example

import androidx.compose.ui.window.ComposeUIViewController
import zone.ien.firebase.FirebasePlatformContext

fun MainViewController() = ComposeUIViewController { App(context = FirebasePlatformContext()) }