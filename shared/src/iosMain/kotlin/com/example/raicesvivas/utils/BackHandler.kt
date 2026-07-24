package com.example.raicesvivas.utils

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS handles back navigation differently (usually via swipe-to-back in UINavigationController)
    // Compose Multiplatform for iOS doesn't have a direct equivalent of BackHandler yet
}
