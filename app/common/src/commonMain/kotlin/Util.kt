import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle

fun TextStyle.withTrim(): TextStyle = this.copy(
    lineHeightStyle = (this.lineHeightStyle ?: LineHeightStyle.Default).copy(trim = LineHeightStyle.Trim.Both)
)
