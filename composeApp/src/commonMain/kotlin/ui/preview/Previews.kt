package ui.preview

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Dark",
    group = "Component",
    backgroundColor = 0xFF000000,
    showBackground = true,
    uiMode = 0x20
)
@Preview(
    name = "Light",
    group = "Component",
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
annotation class ComponentPreview()

@Preview(
    name = "Phone Dark",
    group = "Phone",
    uiMode = 0x20,
    device = "spec:width=411dp,height=891dp"
)
@Preview(
    name = "Phone Light",
    group = "Phone",
    device = "spec:width=411dp,height=891dp"
)
@Preview(
    name = "Tablet Dark",
    group = "Tablet",
    uiMode = 0x20,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Preview(
    name = "Tablet Light",
    group = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
annotation class DevicePreview()
