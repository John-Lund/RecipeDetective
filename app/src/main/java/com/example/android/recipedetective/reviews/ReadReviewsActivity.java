package com.example.android.recipedetective.reviews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.database.FavouritesObject;
import com.example.android.recipedetective.databinding.ActivityReadReviewsBinding;
import com.example.android.recipedetective.model.ReviewObject;
import com.example.android.recipedetective.repo.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class ReadReviewsActivity extends AppCompatActivity implements ReviewAdapter.ItemClickListener {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference(Constants.REVIEWS);
    private ActivityReadReviewsBinding mBinding;
    private ReadReviewActivityViewModel mViewModel;
    private ArrayList<ReviewObject> mReviews;
    private ReviewAdapter mAdapter;
    private List<FavouritesObject> mFavouritesObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_read_reviews);
        mViewModel = ViewModelProviders.of(this).get(ReadReviewActivityViewModel.class);
        setObserverForFavouritesData();
        setUpUi();
        setUpAdapter();
        getData();
        if (!internetIsOk()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            ReadReviewsActivity.this,
                            getString(R.string.read_reviews_activity_internet_warning),
                            Toast.LENGTH_LONG).show();
                }
            }, 2000);
        }
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


    private void setUpUi() {
        setSupportActionBar(mBinding.readReviewsActivityToolbar);
        mBinding.readReviewsActivityToolbar.setNavigationIcon(R.drawable.svg_baseline_arrow_back_ios___px);
        mBinding.readReviewsActivityToolbar.setTitle("");
        mBinding.readReviewsActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setUpAdapter() {
        mAdapter = new ReviewAdapter(this, this);
        mBinding.readReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.readReviewsRecyclerView.setAdapter(mAdapter);
    }


    private void getData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return;
                }
                mReviews = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (DataSnapshot childData : data.getChildren()) {
                        ReviewObject reviewObject = childData.getValue(ReviewObject.class);
                        mReviews.add(reviewObject);
                    }
                }
                for (int i = 1; i < mReviews.size(); i++) {
                    if (mReviews.get(i - 1).getRecipeId().equals(mReviews.get(i).getRecipeId())) {
                        mReviews.get(i).setHeaderValue(Constants.HEADERLESS);
                    } else {
                        mReviews.get(i - 1).setFooterValue(Constants.FOOTERLESS);
                    }
                }
                for (ReviewObject review : mReviews) {
                    review.setRecipeIsFavourite(checkIfRecipeIsFavourite(review.getRecipeId()));
                    switch (review.getUserRating()) {
                        case 0:
                            review.setReviewStarsImageId(R.drawable.nostarsnew);
                            break;
                        case 1:
                            review.setReviewStarsImageId(R.drawable.onestarnew);
                            break;
                        case 2:
                            review.setReviewStarsImageId(R.drawable.twostarsnew);
                            break;
                        case 3:
                            review.setReviewStarsImageId(R.drawable.threestarsnew);
                            break;
                        case 4:
                            review.setReviewStarsImageId(R.drawable.fourstarsnew);
                            break;
                        default:
                            review.setReviewStarsImageId(R.drawable.fivestarsnew);
                            break;
                    }
                }
                mAdapter.setData(mReviews);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean checkIfRecipeIsFavourite(String recipeId) {
        for (FavouritesObject favourite : mFavouritesObjects) {
            if (favourite.getRecipeId().equals(recipeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(ReadReviewsActivity.this, "up-to-bottom");
    }

    @Override
    public void onItemClick(int recipeIndex) {
        mViewModel.createFavourite(mReviews.get(recipeIndex));
        Toast.makeText(ReadReviewsActivity.this,
                "" + mReviews.get(recipeIndex).getRecipeTitle() + getString(R.string.favourite_added_message),
                Toast.LENGTH_SHORT).show();

    }

    private void setObserverForFavouritesData() {
        mViewModel.getFavouritesList().observe(this, new Observer<List<FavouritesObject>>() {
            @Override
            public void onChanged(@Nullable List<FavouritesObject> favouritesObjects) {
                mFavouritesObjects = favouritesObjects;
            }
        });
    }
}
