package com.kyokosawada.ui.utils

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * WindowSizeClass implementation following Material Design 3 guidelines
 * Based on Android's official Compose Samples (Reply app)
 */
@Immutable
data class WindowSizeClass(
    val widthSizeClass: WindowWidthSizeClass,
    val heightSizeClass: WindowHeightSizeClass
)

/**
 * Width-based size classes
 */
enum class WindowWidthSizeClass {
    Compact,  // < 600dp (phones in portrait)
    Medium,   // 600dp-840dp (phones in landscape, small tablets)
    Expanded  // > 840dp (large tablets, desktops)
}

/**
 * Height-based size classes
 */
enum class WindowHeightSizeClass {
    Compact,  // < 480dp (phones in landscape)
    Medium,   // 480dp-900dp (phones in portrait, small tablets)
    Expanded  // > 900dp (large tablets, desktops)
}

/**
 * Device type and orientation classifications
 */
enum class DeviceType {
    MobilePortrait,     // Compact width + Medium/Expanded height
    MobileLandscape,    // Medium width + Compact height
    TabletPortrait,     // Medium/Expanded width + Expanded height
    TabletLandscape,    // Expanded width + Medium height
    Desktop             // Expanded width + Expanded height (very large)
}

/**
 * Calculates the current WindowSizeClass based on available space
 */
@Composable
fun calculateWindowSizeClass(): WindowSizeClass {
    var windowSizeClass: WindowSizeClass? = null

    BoxWithConstraints {
        val width = maxWidth
        val height = maxHeight

        val widthSizeClass = when {
            width < 600.dp -> WindowWidthSizeClass.Compact
            width < 840.dp -> WindowWidthSizeClass.Medium
            else -> WindowWidthSizeClass.Expanded
        }

        val heightSizeClass = when {
            height < 480.dp -> WindowHeightSizeClass.Compact
            height < 900.dp -> WindowHeightSizeClass.Medium
            else -> WindowHeightSizeClass.Expanded
        }

        windowSizeClass = WindowSizeClass(widthSizeClass, heightSizeClass)
    }

    return windowSizeClass ?: WindowSizeClass(
        WindowWidthSizeClass.Compact,
        WindowHeightSizeClass.Medium
    )
}

/**
 * Determines device type and orientation from WindowSizeClass
 */
fun WindowSizeClass.toDeviceType(): DeviceType {
    return when {
        // Mobile Portrait: Narrow width, tall height
        widthSizeClass == WindowWidthSizeClass.Compact &&
                (heightSizeClass == WindowHeightSizeClass.Medium || heightSizeClass == WindowHeightSizeClass.Expanded) ->
            DeviceType.MobilePortrait

        // Mobile Landscape: Medium width, compact height
        widthSizeClass == WindowWidthSizeClass.Medium &&
                heightSizeClass == WindowHeightSizeClass.Compact ->
            DeviceType.MobileLandscape

        // Tablet Portrait: Medium+ width, expanded height
        (widthSizeClass == WindowWidthSizeClass.Medium || widthSizeClass == WindowWidthSizeClass.Expanded) &&
                heightSizeClass == WindowHeightSizeClass.Expanded ->
            DeviceType.TabletPortrait

        // Tablet Landscape: Expanded width, medium height
        widthSizeClass == WindowWidthSizeClass.Expanded &&
                heightSizeClass == WindowHeightSizeClass.Medium ->
            DeviceType.TabletLandscape

        // Desktop: Large width and height
        widthSizeClass == WindowWidthSizeClass.Expanded &&
                heightSizeClass == WindowHeightSizeClass.Expanded ->
            DeviceType.Desktop

        else -> DeviceType.MobilePortrait // Default fallback
    }
}

/**
 * Container for all adaptive values
 */
@Immutable
data class AdaptiveValues(
    val horizontalPadding: Dp,
    val cardMaxWidth: Dp,
    val cardPadding: Dp,
    val logoSize: Dp,
    val buttonHeight: Dp,
    val spacingScale: Float,
    val isCompactLayout: Boolean,
    val cardContentSpacing: Dp,
    val formFieldSpacing: Dp,
    val dividerSpacing: Dp,
    val altAuthSpacing: Dp
)

/**
 * Get adaptive values based on WindowSizeClass
 */
fun WindowSizeClass.getAdaptiveValues(): AdaptiveValues {
    val deviceType = toDeviceType()

    return when (deviceType) {
        DeviceType.MobilePortrait -> AdaptiveValues(
            horizontalPadding = 16.dp,
            cardMaxWidth = Dp.Infinity,
            cardPadding = 20.dp,
            logoSize = 72.dp,
            buttonHeight = 56.dp,
            spacingScale = 1.0f,
            isCompactLayout = true,
            cardContentSpacing = 12.dp,     // Tight spacing between major sections
            formFieldSpacing = 12.dp,       // Keep reasonable for form fields
            dividerSpacing = 8.dp,          // Minimal space around divider
            altAuthSpacing = 0.dp           // NO SPACE between auth alternatives!
        )

        DeviceType.MobileLandscape -> AdaptiveValues(
            horizontalPadding = 24.dp,
            cardMaxWidth = 480.dp,
            cardPadding = 16.dp,
            logoSize = 64.dp,
            buttonHeight = 48.dp,
            spacingScale = 0.8f,
            isCompactLayout = true,
            cardContentSpacing = 8.dp,      // Very tight for landscape
            formFieldSpacing = 8.dp,        // Minimal for landscape efficiency  
            dividerSpacing = 6.dp,          // Extremely tight for landscape
            altAuthSpacing = 0.dp           // NO SPACE - completely connected
        )

        DeviceType.TabletPortrait -> AdaptiveValues(
            horizontalPadding = 32.dp,
            cardMaxWidth = 520.dp,
            cardPadding = 32.dp,
            logoSize = 96.dp,
            buttonHeight = 56.dp,
            spacingScale = 1.2f,
            isCompactLayout = false,
            cardContentSpacing = 16.dp,     // Reasonable for tablets
            formFieldSpacing = 12.dp,       // Tighter forms
            dividerSpacing = 8.dp,          // Minimal space
            altAuthSpacing = 0.dp           // NO SPACE - connected elements
        )

        DeviceType.TabletLandscape -> AdaptiveValues(
            horizontalPadding = 48.dp,
            cardMaxWidth = 600.dp,
            cardPadding = 32.dp,
            logoSize = 96.dp,
            buttonHeight = 56.dp,
            spacingScale = 1.2f,
            isCompactLayout = false,
            cardContentSpacing = 16.dp,     // Reasonable spacing
            formFieldSpacing = 12.dp,       // Consistent with portrait
            dividerSpacing = 8.dp,          // Minimal space  
            altAuthSpacing = 0.dp           // NO SPACE - tight grouping
        )

        DeviceType.Desktop -> AdaptiveValues(
            horizontalPadding = 64.dp,
            cardMaxWidth = 640.dp,
            cardPadding = 40.dp,
            logoSize = 112.dp,
            buttonHeight = 56.dp,
            spacingScale = 1.4f,
            isCompactLayout = false,
            cardContentSpacing = 20.dp,     // Generous but not excessive
            formFieldSpacing = 16.dp,       // Good spacing for desktop
            dividerSpacing = 12.dp,         // Reasonable space around divider
            altAuthSpacing = 2.dp           // Minimal but visible - desktop can handle tiny space
        )
    }
}