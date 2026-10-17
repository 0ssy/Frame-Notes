val ForestGreen = Color(0xFF5E6C5B)
val FreshCream  = Color(0xFFF4EFE6)
val CloudWhite  = Color(0xFFFEFCF6)
val SkyBlue     = Color(0xFFD6E0E2)
val StormCloud  = Color(0xFF686867)
val Midnight    = Color(0xFF162A2C)

private val Light = lightColorScheme(
    primary = ForestGreen, onPrimary = CloudWhite,
    secondaryContainer = SkyBlue, onSecondaryContainer = Midnight,
    background = CloudWhite, onBackground = Midnight,
    surface = FreshCream, onSurface = Midnight,
    onSurfaceVariant = StormCloud,
)
private val Dark = darkColorScheme(
    primary = SkyBlue, onPrimary = Midnight,
    secondaryContainer = ForestGreen, onSecondaryContainer = CloudWhite,
    background = Midnight, onBackground = CloudWhite,
    surface = Color(0xFF1D3335), onSurface = CloudWhite,
    onSurfaceVariant = Color(0xFFA8A8A7),
)

@Composable
fun FrameNotesTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) =
    MaterialTheme(colorScheme = if (dark) Dark else Light, content = content)
