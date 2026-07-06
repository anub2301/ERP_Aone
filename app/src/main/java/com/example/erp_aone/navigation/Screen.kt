package com.example.erp_aone.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {
    @Serializable
    data object Splash : Screen()
    
    @Serializable
    data object Login : Screen()
    
    @Serializable
    data object CompanySelection : Screen()
    
    @Serializable
    data object Dashboard : Screen()

    @Serializable
    data object ProductList : Screen()

    @Serializable
    data object AddProduct : Screen()

    @Serializable
    data object SaleEntry : Screen()

    @Serializable
    data object BillsList : Screen()
}