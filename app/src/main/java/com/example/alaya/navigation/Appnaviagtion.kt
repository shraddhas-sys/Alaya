package com.example.alaya.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alaya.view.*

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot"

    const val DASHBOARD = "dashboard"
    const val PLANNER = "planner"
    const val MEALS = "meals"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) { SplashUI(navController) }
        composable(Routes.ONBOARDING) { OnboardingUI(navController) }
        composable(Routes.LOGIN) { LoginUI(navController) }
        composable(Routes.REGISTER) { RegistrationScreen(navController) }
        composable(Routes.FORGOT) { Forgot(navController) }
        composable(Routes.DASHBOARD) { DashboardUI(navController) }

        composable(Routes.PLANNER) {
            PlannerScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MEALS) {
            MealPlannerScreen(navController = navController)
        }
    }


}

