package zone.ien.firebase.example.ui.screen.placeholder
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.interactive.IenTextFieldState
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenDotCheckbox
import zone.ien.utils.ui.feedback.IenCircularProgressIndicator
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.screen.IenTopAppBar
import zone.ien.utils.ui.screen.IenBackButton
import zone.ien.utils.ui.wrapper.IenRootWrapper
import zone.ien.utils.ui.dialog.IenConfirmDialog
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.TextStyle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun PlaceholderScreen(
    title: String,
    description: String,
    onBack: () -> Unit
) {
    IenScaffold(
        topBar = {
            IenTopAppBar(

                navigationIcon = { IenBackButton(onClick = onBack) },
                title = {
                    IenText(
                        text = title,
                        style = IenTheme.typography.title1,
                        color = IenTheme.colors.textPrimary
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            IenSurface(color = IenTheme.colors.surfaceVariant, 
                modifier = Modifier.fillMaxWidth(),
                
                shape = ContinuousRoundedRectangle(16.dp)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    IenText(
                        text = "Placeholder Screen",
                        style = IenTheme.typography.title2,
                        color = IenTheme.colors.brand
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    IenText(
                        text = description,
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.textPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
