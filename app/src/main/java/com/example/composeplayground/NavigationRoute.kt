package com.example.composeplayground

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    data object Main : NavigationRoute()
    @Serializable
    data object Basics : NavigationRoute()
    @Serializable
    data object Drawing : NavigationRoute()
    @Serializable
    data object Graph : NavigationRoute()
}