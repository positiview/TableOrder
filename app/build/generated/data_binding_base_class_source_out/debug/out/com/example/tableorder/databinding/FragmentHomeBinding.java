// Generated by view binder compiler. Do not edit!
package com.example.tableorder.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.tableorder.R;
import com.google.android.material.card.MaterialCardView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final MaterialCardView cardAboutus;

  @NonNull
  public final MaterialCardView cardContact;

  @NonNull
  public final MaterialCardView cardFoodmap;

  @NonNull
  public final MaterialCardView cardLogout;

  @NonNull
  public final MaterialCardView cardMyPin;

  @NonNull
  public final MaterialCardView cardReceive;

  private FragmentHomeBinding(@NonNull ScrollView rootView, @NonNull MaterialCardView cardAboutus,
      @NonNull MaterialCardView cardContact, @NonNull MaterialCardView cardFoodmap,
      @NonNull MaterialCardView cardLogout, @NonNull MaterialCardView cardMyPin,
      @NonNull MaterialCardView cardReceive) {
    this.rootView = rootView;
    this.cardAboutus = cardAboutus;
    this.cardContact = cardContact;
    this.cardFoodmap = cardFoodmap;
    this.cardLogout = cardLogout;
    this.cardMyPin = cardMyPin;
    this.cardReceive = cardReceive;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cardAboutus;
      MaterialCardView cardAboutus = ViewBindings.findChildViewById(rootView, id);
      if (cardAboutus == null) {
        break missingId;
      }

      id = R.id.cardContact;
      MaterialCardView cardContact = ViewBindings.findChildViewById(rootView, id);
      if (cardContact == null) {
        break missingId;
      }

      id = R.id.cardFoodmap;
      MaterialCardView cardFoodmap = ViewBindings.findChildViewById(rootView, id);
      if (cardFoodmap == null) {
        break missingId;
      }

      id = R.id.cardLogout;
      MaterialCardView cardLogout = ViewBindings.findChildViewById(rootView, id);
      if (cardLogout == null) {
        break missingId;
      }

      id = R.id.cardMyPin;
      MaterialCardView cardMyPin = ViewBindings.findChildViewById(rootView, id);
      if (cardMyPin == null) {
        break missingId;
      }

      id = R.id.cardReceive;
      MaterialCardView cardReceive = ViewBindings.findChildViewById(rootView, id);
      if (cardReceive == null) {
        break missingId;
      }

      return new FragmentHomeBinding((ScrollView) rootView, cardAboutus, cardContact, cardFoodmap,
          cardLogout, cardMyPin, cardReceive);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
