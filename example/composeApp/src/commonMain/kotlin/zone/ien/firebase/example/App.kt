package zone.ien.firebase.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold {
            Column(
                modifier = Modifier.padding(it)
            ) {
                TextButton(
                    onClick = {}
                ) {
                    Text(text = "Hello World!")
                }
            }
        }
    }
}