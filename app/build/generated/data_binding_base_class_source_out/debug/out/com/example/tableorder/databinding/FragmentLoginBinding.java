// Generated by view binder compiler. Do not edit!
package com.example.tableorder.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.tableorder.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton btnLogin;

  @NonNull
  public final TextInputEditText emailEditText;

  @NonNull
  public final TextInputLayout emailTinputLayout;

  @NonNull
  public final TextView forgotPasswordTv;

  @NonNull
  public final ImageView imageView2;

  @NonNull
  public final TextInputEditText passwordEditText;

  @NonNull
  public final TextInputLayout passwordInputLayout;

  @NonNull
  public final ProgressBar progressCircular;

  @NonNull
  public final TextView registerTv;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView2;

  private FragmentLoginBinding(@NonNull ConstraintLayout rootView, @NonNull MaterialButton btnLogin,
      @NonNull TextInputEditText emailEditText, @NonNull TextInputLayout emailTinputLayout,
      @NonNull TextView forgotPasswordTv, @NonNull ImageView imageView2,
      @NonNull TextInputEditText passwordEditText, @NonNull TextInputLayout passwordInputLayout,
      @NonNull ProgressBar progressCircular, @NonNull TextView registerTv,
      @NonNull TextView textView, @NonNull TextView textView2) {
    this.rootView = rootView;
    this.btnLogin = btnLogin;
    this.emailEditText = emailEditText;
    this.emailTinputLayout = emailTinputLayout;
    this.forgotPasswordTv = forgotPasswordTv;
    this.imageView2 = imageView2;
    this.passwordEditText = passwordEditText;
    this.passwordInputLayout = passwordInputLayout;
    this.progressCircular = progressCircular;
    this.registerTv = registerTv;
    this.textView = textView;
    this.textView2 = textView2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnLogin;
      MaterialButton btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.emailEditText;
      TextInputEditText emailEditText = ViewBindings.findChildViewById(rootView, id);
      if (emailEditText == null) {
        break missingId;
      }

      id = R.id.emailTinputLayout;
      TextInputLayout emailTinputLayout = ViewBindings.findChildViewById(rootView, id);
      if (emailTinputLayout == null) {
        break missingId;
      }

      id = R.id.forgotPasswordTv;
      TextView forgotPasswordTv = ViewBindings.findChildViewById(rootView, id);
      if (forgotPasswordTv == null) {
        break missingId;
      }

      id = R.id.imageView2;
      ImageView imageView2 = ViewBindings.findChildViewById(rootView, id);
      if (imageView2 == null) {
        break missingId;
      }

      id = R.id.passwordEditText;
      TextInputEditText passwordEditText = ViewBindings.findChildViewById(rootView, id);
      if (passwordEditText == null) {
        break missingId;
      }

      id = R.id.passwordInputLayout;
      TextInputLayout passwordInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (passwordInputLayout == null) {
        break missingId;
      }

      id = R.id.progress_circular;
      ProgressBar progressCircular = ViewBindings.findChildViewById(rootView, id);
      if (progressCircular == null) {
        break missingId;
      }

      id = R.id.registerTv;
      TextView registerTv = ViewBindings.findChildViewById(rootView, id);
      if (registerTv == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      return new FragmentLoginBinding((ConstraintLayout) rootView, btnLogin, emailEditText,
          emailTinputLayout, forgotPasswordTv, imageView2, passwordEditText, passwordInputLayout,
          progressCircular, registerTv, textView, textView2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
