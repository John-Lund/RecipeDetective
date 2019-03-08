package com.example.android.recipedetective.favourites;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.databinding.ActivityFavouritesDetailsBinding;
import com.example.android.recipedetective.reviews.WriteReviewActivity;
import com.squareup.picasso.Picasso;

import maes.tech.intentanim.CustomIntent;
import widget.RecipeWidgetProvider;

public class FavouritesDetailsActivity extends AppCompatActivity {
    private ActivityFavouritesDetailsBinding mBinding;
    private FavouritesDetailsViewModel mViewModel;
    private FavouritesObject mCurrentFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourites_details);
        mViewModel = ViewModelProviders.of(this).get(FavouritesDetailsViewModel.class);
        mCurrentFavourite = mViewModel.getCurrentFavouritesObject();
        // resetting the object the widget will use for its data
        mViewModel.setCurrentWidgetDataObject(mCurrentFavourite.getRecipeName(), mCurrentFavourite.getBasicIngredientsList());
        setUpUI();
        // telling the widget to reload its data
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(this, RecipeWidgetProvider.class));
        this.sendBroadcast(intent);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(FavouritesDetailsActivity.this, "right-to-left");
    }

    private void setUpUI() {
        setSupportActionBar(mBinding.favouritesDetailsActivityToolbar);
        mBinding.favouritesDetailsActivityToolbar.setNavigationIcon(R.drawable.svg_baseline_arrow_back_ios___px);
        mBinding.favouritesDetailsActivityToolbar.setTitle("");
        mBinding.favouritesDetailsActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.favouritesDetailsRecipeNameText.setText(mCurrentFavourite.getRecipeName());
        mBinding.favouritesDetailsServingsText.setText(mCurrentFavourite.getServings());
        mBinding.favouritesDetailsPrepTimeText.setText(mCurrentFavourite.getTotalTime());
        mBinding.favouritesDetailsIngredientsListText.setText(mCurrentFavourite.getDetailedIngredientsList());
        Picasso.with(this)
                .load(mCurrentFavourite.getImageUrl())
                .into(mBinding.favouritesDetailsRecipeImage);
        View.OnClickListener detailsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.favourites_details_site_link_button:
                        Uri webPage = Uri.parse(mCurrentFavourite.getSourceRecipeUrl());
                        intent = new Intent(Intent.ACTION_VIEW, webPage);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        break;
                    case R.id.remove_favourite_button:
                        mBinding.removeFavouriteButton.setVisibility(View.GONE);
                        mBinding.favouritesDetailsBookmarkIcon.setVisibility(View.GONE);
                        mViewModel.deleteCurrentFavouritesObject();
                        Toast.makeText(getApplication(), getString(R.string.recipe_deleted_message), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        mBinding.favouritesDetailsSiteLinkButton.setOnClickListener(detailsListener);
        mBinding.removeFavouriteButton.setOnClickListener(detailsListener);
        final Typeface pressed = Typeface.createFromAsset(getAssets(), "font/pt_serif_web_bolditalic.ttf");
        final Typeface idle = Typeface.createFromAsset(getAssets(), "font/pt_serif_web_italic.ttf");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.writeReviewTextView.setTypeface(pressed);
                mBinding.writeReviewTextView.setTextColor(Color.parseColor("#BD0003"));
                mBinding.writeReviewIcon.setImageDrawable(getResources().getDrawable(R.drawable.write_review_icon_pressed));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.writeReviewTextView.setTypeface(idle);
                        mBinding.writeReviewTextView.setTextColor(Color.parseColor("#89A046"));
                        mBinding.writeReviewIcon.setImageDrawable(getResources().getDrawable(R.drawable.write_review_icon_idle));
                        if (internetIsOk()) {
                            Intent intent = new Intent(getApplication(), WriteReviewActivity.class);
                            startActivity(intent);
                            CustomIntent.customType(FavouritesDetailsActivity.this, "left-to-right");
                        } else {
                            Toast.makeText(FavouritesDetailsActivity.this,
                                    getString(R.string.internet_warning_review_activity),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 500);
            }
        };
        mBinding.writeReviewIcon.setOnClickListener(onClickListener);
        mBinding.writeReviewTextView.setOnClickListener(onClickListener);
    }

    // method to check internet connection
    private boolean internetIsOk() {
        ConnectivityManager manager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;
        activeNetwork = (manager != null) ? manager.getActiveNetworkInfo() : null;
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
