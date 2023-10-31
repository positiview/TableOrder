package com.example.tableorder.fragments.home

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tableorder.R

public class HomeFragment1Directions private constructor() {
  public companion object {
    public fun actionHomeFragmentToLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_loginFragment)

    public fun actionHomeFragmentToAboutUsFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_aboutUsFragment)

    public fun actionHomeFragmentToFoodMapFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_foodMapFragment)

    public fun actionHomeFragmentToAboutUsFragment2(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_aboutUsFragment2)

    public fun actionHomeFragmentToContactUsFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_contactUsFragment)
  }
}
