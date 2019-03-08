package com.example.android.recipedetective;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.recipedetective.databinding.ActivityMainBinding;
import com.example.android.recipedetective.favourites.FavouritesActivity;
import com.example.android.recipedetective.reviews.ReadReviewsActivity;
import com.example.android.recipedetective.search.SearchActivity;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private MainActivityViewModel mViewModel;
    private Handler mHandler = new Handler();

    // clearing search results from search activity when user comes back to this activity
    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               mViewModel.clearCurrentSearchResults();
            }
        }, 2000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.setUpDatabase(getApplication());
        setUpButtonListener();
    }
    // setting up a listener to animate button clicks and start activities
    private void setUpButtonListener() {
        final Typeface pressed = Typeface.createFromAsset(getAssets(), "font/opensans_extrabold.ttf");
        final Typeface idle = Typeface.createFromAsset(getAssets(), "font/opensans_regular.ttf");
        View.OnClickListener mainListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.search_button_main:
                        mBinding.searchButtonMain.setTextColor(getResources().getColor(R.color.mainTextPressed));
                        mBinding.searchManifierMain.setImageDrawable(getResources().getDrawable(R.drawable.search_manifier_pressed));
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.searchManifierMain.setImageDrawable(getResources().getDrawable(R.drawable.search_manifier_idle));
                                mBinding.searchButtonMain.setTextColor(getResources().getColor(R.color.mainTextIdle));
                                Intent intent = new Intent(getApplication(), SearchActivity.class);
                                startActivity(intent);
                                CustomIntent.customType(MainActivity.this, "left-to-right");
                            }
                        }, 200);
                        break;
                    case R.id.main_fav_image_button:
                        mBinding.mainMyTextView.setTypeface(pressed);
                        mBinding.mainMyTextView.setTextColor(Color.parseColor("#2C2525"));
                        mBinding.mainFavImageButton.setAlpha(0.0f);
                        mBinding.mainFavIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favourite_pressed_24px));
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.mainFavImageButton.setAlpha(1.0f);
                                mBinding.mainMyTextView.setTypeface(idle);
                                mBinding.mainMyTextView.setTextColor(Color.parseColor("#FFE8DC"));
                                mBinding.mainFavIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favourite_idle_24px));
                                Intent intent = new Intent(getApplication(), FavouritesActivity.class);
                                startActivity(intent);
                                CustomIntent.customType(MainActivity.this, "bottom-to-up");
                            }
                        }, 200);
                        break;
                    case R.id.main_reviews_image_button:
                        mBinding.mainReadReviewsTextView.setTypeface(pressed);
                        mBinding.mainReviewsImageButton.setAlpha(0.0f);
                        mBinding.mainReviewsIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_rate_review_pressed_24px));
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.mainReviewsImageButton.setAlpha(1.0f);
                                mBinding.mainReadReviewsTextView.setTypeface(idle);
                                mBinding.mainReviewsIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_rate_review_idle_24px));
                                Intent intent = new Intent(getApplication(), ReadReviewsActivity.class);
                                startActivity(intent);
                                CustomIntent.customType(MainActivity.this, "bottom-to-up");
                            }
                        }, 200);
                        break;
                }
            }
        };
        mBinding.searchButtonMain.setOnClickListener(mainListener);
        mBinding.mainFavImageButton.setOnClickListener(mainListener);
        mBinding.mainReviewsImageButton.setOnClickListener(mainListener);
    }
}


