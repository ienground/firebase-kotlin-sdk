package zone.ien.firebase.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import zone.ien.utils.ui.foundation.IenTheme

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    IenTheme(
        darkTheme = darkTheme,
        content = content
    )
}
