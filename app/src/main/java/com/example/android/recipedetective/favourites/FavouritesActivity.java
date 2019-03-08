package com.example.android.recipedetective.favourites;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.databinding.ActivityFavouritesBinding;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class FavouritesActivity extends AppCompatActivity implements FavouritesAdapter.ItemClickListener {
    private FavouritesActivityViewModel mViewModel;
    private ActivityFavouritesBinding mBinding;
    private FavouritesAdapter mFavouritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourites);
        setUpUi();
        setUpAdapter();
        setUpViewModel();
    }

    private void setUpUi() {
        setSupportActionBar(mBinding.favouritesActivityToolbar);
        mBinding.favouritesActivityToolbar.setNavigationIcon(R.drawable.svg_baseline_arrow_back_ios___px);
        mBinding.favouritesActivityToolbar.setTitle("");
        mBinding.favouritesActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpAdapter() {
        mFavouritesAdapter = new FavouritesAdapter(this, this);
        mBinding.favouritesRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.favourites_recycler_column_count)));
        mBinding.favouritesRecyclerView.setAdapter(mFavouritesAdapter);
    }

    private void setUpViewModel() {
        mViewModel = ViewModelProviders.of(this).get(FavouritesActivityViewModel.class);
        mViewModel.getFavouritesLiveData().observe(this, new Observer<List<FavouritesObject>>() {
            @Override
            public void onChanged(@Nullable List<FavouritesObject> favouritesObjects) {
                mViewModel.setFavouritesList(favouritesObjects);
                if (mFavouritesAdapter != null) {
                    mFavouritesAdapter.setData(favouritesObjects);
                 }
            }
        });
    }


    @Override
    public void onItemClick(int recipeIndex) {
        mViewModel.setCurrentFavouritesObject(recipeIndex);
        Intent intent = new Intent(this, FavouritesDetailsActivity.class);
        startActivity(intent);
        CustomIntent.customType(FavouritesActivity.this, "left-to-right");
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(FavouritesActivity.this, "up-to-bottom");
    }
}
