package com.example.tableorder.members

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.tableorder.R

public class SignUpFragmentDirections private constructor() {
  public companion object {
    public fun actionSignUpFragmentToLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_signUpFragment_to_loginFragment)
  }
}
