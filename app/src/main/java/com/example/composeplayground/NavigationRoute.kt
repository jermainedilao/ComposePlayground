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
    @Serializable
    data object FillInTheBlanks : NavigationRoute()
    @Serializable
    data object FillInTheBlanksV2 : NavigationRoute()
    @Serializable
    data object FillInTheBlanksV3 : NavigationRoute()
    @Serializable
    data object FillInTheBlanksV4 : NavigationRoute()
    @Serializable
    data object Coil : NavigationRoute()
}