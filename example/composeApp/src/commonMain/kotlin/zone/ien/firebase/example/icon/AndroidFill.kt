package zone.ien.firebase.example.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.AndroidFill: ImageVector
    get() {
        if (_AndroidFill != null) {
            return _AndroidFill!!
        }
        _AndroidFill = ImageVector.Builder(
            name = "AndroidFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(6.382f, 3.968f)
                curveTo(7.922f, 2.736f, 9.875f, 2f, 12f, 2f)
                curveTo(14.125f, 2f, 16.078f, 2.736f, 17.618f, 3.968f)
                lineTo(19.071f, 2.515f)
                lineTo(20.485f, 3.929f)
                lineTo(19.032f, 5.382f)
                curveTo(20.264f, 6.922f, 21f, 8.875f, 21f, 11f)
                verticalLineTo(12f)
                horizontalLineTo(3f)
                verticalLineTo(11f)
                curveTo(3f, 8.875f, 3.736f, 6.922f, 4.968f, 5.382f)
                lineTo(3.515f, 3.929f)
                lineTo(4.929f, 2.515f)
                lineTo(6.382f, 3.968f)
                close()
                moveTo(3f, 14f)
                horizontalLineTo(21f)
                verticalLineTo(21f)
                curveTo(21f, 21.552f, 20.552f, 22f, 20f, 22f)
                horizontalLineTo(4f)
                curveTo(3.448f, 22f, 3f, 21.552f, 3f, 21f)
                verticalLineTo(14f)
                close()
                moveTo(9f, 9f)
                curveTo(9.552f, 9f, 10f, 8.552f, 10f, 8f)
                curveTo(10f, 7.448f, 9.552f, 7f, 9f, 7f)
                curveTo(8.448f, 7f, 8f, 7.448f, 8f, 8f)
                curveTo(8f, 8.552f, 8.448f, 9f, 9f, 9f)
                close()
                moveTo(15f, 9f)
                curveTo(15.552f, 9f, 16f, 8.552f, 16f, 8f)
                curveTo(16f, 7.448f, 15.552f, 7f, 15f, 7f)
                curveTo(14.448f, 7f, 14f, 7.448f, 14f, 8f)
                curveTo(14f, 8.552f, 14.448f, 9f, 15f, 9f)
                close()
            }
        }.build()

        return _AndroidFill!!
    }

@Suppress("ObjectPropertyName")
private var _AndroidFill: ImageVector? = null
