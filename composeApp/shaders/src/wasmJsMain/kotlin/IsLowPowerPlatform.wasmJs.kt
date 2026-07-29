@OptIn(ExperimentalWasmJsInterop::class)
private fun detectLowPowerBrowser(): Boolean =
    js(
        """
        (function () {
          if (navigator.userAgentData && typeof navigator.userAgentData.mobile === 'boolean') {
            return navigator.userAgentData.mobile;
          }
          return /Mobi|Android|iPhone|iPad|iPod/i.test(navigator.userAgent);
        })()
        """,
    )

internal actual val isLowPowerPlatform: Boolean = detectLowPowerBrowser()
