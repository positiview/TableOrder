package com.example.tableorder.members

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tableorder.R

public class LoginFragmentDirections private constructor() {
  public companion object {
    public fun actionLoginFragmentToSignUpFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_signUpFragment)

    public fun actionLoginFragmentToHomeFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_homeFragment)

    public fun actionLoginFragmentToResetPasswordFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_resetPasswordFragment)
  }
}
