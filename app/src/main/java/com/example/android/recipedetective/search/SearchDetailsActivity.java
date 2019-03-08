package com.example.android.recipedetective.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.databinding.ActivitySearchDetailsBinding;
import com.example.android.recipedetective.model.DetailsObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class SearchDetailsActivity extends AppCompatActivity {
    private ActivitySearchDetailsBinding mBinding;
    private SearchDetailsViewModel mViewModel;
    private DetailsObject mDetailsObject;
    private boolean mRecipeIsFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_details);
        mBinding.searchDetailsFab.hide();
        mBinding.detailsBookmarkIcon.setVisibility(View.GONE);
        mViewModel = ViewModelProviders.of(this).get(SearchDetailsViewModel.class);
        setUpInitialUI();
        setObserverForDetailsData();
        setObserverForFavouritesData();
    }

    private void setUpInitialUI() {
        setSupportActionBar(mBinding.searchDetailsActivityToolbar);
        mBinding.searchDetailsActivityToolbar.setNavigationIcon(R.drawable.svg_baseline_arrow_back_ios___px);
        mBinding.searchDetailsActivityToolbar.setTitle("");
        mBinding.searchDetailsActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.detailsRecipeNameText.setText(mViewModel.getCurrentSearchObject().getRecipeName());
        mBinding.detailsSourceNameText.setText(mViewModel.getCurrentSearchObject().getSourceDisplayName());
        Picasso.with(this)
                .load(mViewModel.getCurrentSearchObject().getImagineUrlString())
                .into(mBinding.detailsRecipeImage);
    }

    private void setObserverForFavouritesData() {
        mViewModel.getFavouritesList().observe(this, new Observer<List<FavouritesObject>>() {
            @Override
            public void onChanged(@Nullable List<FavouritesObject> favouritesObjects) {
                if (favouritesObjects != null) {
                    for (FavouritesObject favourite : favouritesObjects) {
                        if (favourite.getRecipeId().equals(mViewModel.getCurrentSearchObject().getRecipeId())) {
                            mRecipeIsFavourite = true;
                            mBinding.detailsBookmarkIcon.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
                if (!mRecipeIsFavourite) {
                    mBinding.searchDetailsFab.show();
                    mBinding.detailsBookmarkIcon.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setObserverForDetailsData() {
        mViewModel.getDetails().observe(this, new Observer<DetailsObject>() {
            @Override
            public void onChanged(@Nullable DetailsObject detailsObject) {
                mDetailsObject = detailsObject;
                if (mDetailsObject != null) {
                    mBinding.detailsServingsText.setText(detailsObject.getServings());
                    mBinding.detailsPrepTimeText.setText(detailsObject.getPreparationTime());
                    mBinding.detailsIngredientsListText.setText(detailsObject.getDetailedIngredients());

                    View.OnClickListener detailsListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.detail_site_link_button:
                                    Uri webPage = Uri.parse(mDetailsObject.getSourceRecipeUrl());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(intent);
                                    }
                                    break;
                                case R.id.search_details_fab:
                                    mBinding.searchDetailsFab.hide();
                                    mViewModel.createFavourite();
                                    Toast.makeText(getApplication(), getString(R.string.recipe_added_to_favourites_message), Toast.LENGTH_SHORT).show();
                                    mBinding.detailsBookmarkIcon.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    };
                    mBinding.detailSiteLinkButton.setOnClickListener(detailsListener);
                    mBinding.searchDetailsFab.setOnClickListener(detailsListener);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(SearchDetailsActivity.this, "right-to-left");
    }
}
