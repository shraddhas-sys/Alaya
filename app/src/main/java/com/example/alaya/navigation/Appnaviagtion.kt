package com.example.alaya.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alaya.view.*
import com.example.alaya.view.AlayaNotificationScreen
import com.example.alaya.viewmodel.DashboardViewModel

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot"
    const val DASHBOARD = "dashboard"
    const val PLANNER = "planner"
    const val MEALS = "meals"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val dashboardViewModel: DashboardViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) { SplashUI(navController) }
        composable(Routes.ONBOARDING) { OnboardingUI(navController) }
        composable(Routes.LOGIN) { LoginUI(navController) }
        composable(Routes.REGISTER) { RegistrationScreen(navController) }
        composable(Routes.FORGOT) { Forgot(navController) }
        composable(Routes.DASHBOARD) {
            DashboardUI(navController, dashboardViewModel)
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PLANNER) {
            PlannerScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.MEALS) {
            MealPlannerContent(onBack = { navController.popBackStack() })
        }
        composable(Routes.NOTIFICATIONS) {
            AlayaNotificationScreen(
                navController = navController,
                viewModel = dashboardViewModel
            )
        }
    }
}