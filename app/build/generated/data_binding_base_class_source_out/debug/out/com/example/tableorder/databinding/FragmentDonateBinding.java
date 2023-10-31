// Generated by view binder compiler. Do not edit!
package com.example.tableorder.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.tableorder.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDonateBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final TextInputLayout decriptionError;

  @NonNull
  public final TextInputEditText description;

  @NonNull
  public final TextInputEditText donorname;

  @NonNull
  public final TextInputEditText fooditem;

  @NonNull
  public final TextInputLayout itemError;

  @NonNull
  public final TextInputLayout nameError;

  @NonNull
  public final TextInputEditText phone;

  @NonNull
  public final TextInputLayout phoneError;

  @NonNull
  public final ProgressBar progressCircular;

  @NonNull
  public final MaterialButton submit;

  @NonNull
  public final Toolbar toolbar;

  private FragmentDonateBinding(@NonNull ScrollView rootView,
      @NonNull TextInputLayout decriptionError, @NonNull TextInputEditText description,
      @NonNull TextInputEditText donorname, @NonNull TextInputEditText fooditem,
      @NonNull TextInputLayout itemError, @NonNull TextInputLayout nameError,
      @NonNull TextInputEditText phone, @NonNull TextInputLayout phoneError,
      @NonNull ProgressBar progressCircular, @NonNull MaterialButton submit,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.decriptionError = decriptionError;
    this.description = description;
    this.donorname = donorname;
    this.fooditem = fooditem;
    this.itemError = itemError;
    this.nameError = nameError;
    this.phone = phone;
    this.phoneError = phoneError;
    this.progressCircular = progressCircular;
    this.submit = submit;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDonateBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDonateBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_donate, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDonateBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.decription_error;
      TextInputLayout decriptionError = ViewBindings.findChildViewById(rootView, id);
      if (decriptionError == null) {
        break missingId;
      }

      id = R.id.description;
      TextInputEditText description = ViewBindings.findChildViewById(rootView, id);
      if (description == null) {
        break missingId;
      }

      id = R.id.donorname;
      TextInputEditText donorname = ViewBindings.findChildViewById(rootView, id);
      if (donorname == null) {
        break missingId;
      }

      id = R.id.fooditem;
      TextInputEditText fooditem = ViewBindings.findChildViewById(rootView, id);
      if (fooditem == null) {
        break missingId;
      }

      id = R.id.itemError;
      TextInputLayout itemError = ViewBindings.findChildViewById(rootView, id);
      if (itemError == null) {
        break missingId;
      }

      id = R.id.nameError;
      TextInputLayout nameError = ViewBindings.findChildViewById(rootView, id);
      if (nameError == null) {
        break missingId;
      }

      id = R.id.phone;
      TextInputEditText phone = ViewBindings.findChildViewById(rootView, id);
      if (phone == null) {
        break missingId;
      }

      id = R.id.phoneError;
      TextInputLayout phoneError = ViewBindings.findChildViewById(rootView, id);
      if (phoneError == null) {
        break missingId;
      }

      id = R.id.progress_circular;
      ProgressBar progressCircular = ViewBindings.findChildViewById(rootView, id);
      if (progressCircular == null) {
        break missingId;
      }

      id = R.id.submit;
      MaterialButton submit = ViewBindings.findChildViewById(rootView, id);
      if (submit == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FragmentDonateBinding((ScrollView) rootView, decriptionError, description,
          donorname, fooditem, itemError, nameError, phone, phoneError, progressCircular, submit,
          toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
