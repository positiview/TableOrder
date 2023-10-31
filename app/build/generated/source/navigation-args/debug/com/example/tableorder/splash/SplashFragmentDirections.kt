package com.example.tableorder.splash

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tableorder.R

public class SplashFragmentDirections private constructor() {
  public companion object {
    public fun actionSplashFragmentToLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_splashFragment_to_loginFragment)

    public fun actionSplashFragmentToHomeFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_splashFragment_to_homeFragment)
  }
}
