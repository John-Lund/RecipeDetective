package com.example.android.recipedetective.reviews;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.database.FavouritesObject;


import com.example.android.recipedetective.databinding.ActivityWriteReviewBinding;
import com.example.android.recipedetective.model.ReviewObject;
import com.example.android.recipedetective.repo.Constants;
import com.firebase.ui.auth.AuthUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class WriteReviewActivity extends AppCompatActivity {
    private ActivityWriteReviewBinding mBinding;
    private WriteReviewActivityViewModel mViewModel;
    private FavouritesObject mCurrentFavourite;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference(Constants.REVIEWS);
    private boolean mReviewExists = false;
    private int mStarRating = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 5892;
    private boolean mWarningScreenOpen = false;
    public static final String STAR_RATING = "star rating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_write_review);
        mViewModel = ViewModelProviders.of(this).get(WriteReviewActivityViewModel.class);
        mCurrentFavourite = mViewModel.getCurrentFavouritesObject();
        checkForExistingReview();
        if (savedInstanceState != null) {
            mStarRating = savedInstanceState.getInt(STAR_RATING, 0);
            setUpStarValues();
        }
        setUpUI();
        mAuth = FirebaseAuth.getInstance();
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (!mWarningScreenOpen && user == null) {
                    startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    public void setUpStarValues() {
        switch (mStarRating) {
            case 0:
                setStars(0, 0, 0, 0, 0);
                break;
            case 1:
                setStars(1, 0, 0, 0, 0);
                break;
            case 2:
                setStars(1, 1, 0, 0, 0);
                break;
            case 3:
                setStars(1, 1, 1, 0, 0);
                break;
            case 4:
                setStars(1, 1, 1, 1, 0);
                break;
            default:
                setStars(1, 1, 1, 1, 1);
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STAR_RATING, mStarRating);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(WriteReviewActivity.this, "right-to-left");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                signInWarning();
            } else {
                Toast.makeText(WriteReviewActivity.this, getString(R.string.you_are_signed_in), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void signInWarning() {
        mBinding.signInWarningTextView.setVisibility(View.VISIBLE);
        mWarningScreenOpen = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 4000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sign_out) {
            AuthUI.getInstance().signOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private String getUserName() {
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getDisplayName() != null) {
            return mAuth.getCurrentUser().getDisplayName();
        } else {
            return "";
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

    private void setUpUI() {
        setSupportActionBar(mBinding.writeReviewActivityToolbar);
        mBinding.writeReviewActivityToolbar.setNavigationIcon(R.drawable.svg_baseline_arrow_back_ios___px);
        mBinding.writeReviewActivityToolbar.setTitle("");
        mBinding.writeReviewActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.writeEditText.setHintTextColor(getResources().getColor(R.color.colorPrimaryLight));
        mBinding.writeRecipeTitleTextView.setText(mCurrentFavourite.getRecipeName());
        View.OnClickListener reviewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!internetIsOk()) {
                    Toast.makeText(WriteReviewActivity.this,
                            getString(R.string.write_review_internet_connection_warning),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mBinding.writeEditText.getText().toString().length() < 1){
                  Toast.makeText(WriteReviewActivity.this, getString(R.string.empty_review_warning),Toast.LENGTH_SHORT ).show();
                  return;
                }

                if (!mReviewExists) {
                    ReviewObject reviewObject = new ReviewObject(
                            mCurrentFavourite.getRecipeName(),
                            mCurrentFavourite.getBasicIngredientsList(),
                            mCurrentFavourite.getImageUrl(),
                            getUserName(),
                            mCurrentFavourite.getRecipeId(),
                            mBinding.writeEditText.getText().toString(),
                            "",
                            mStarRating,
                            "",
                            mCurrentFavourite.getSourceName(),
                            mCurrentFavourite.getSourceRecipeUrl(),
                            mCurrentFavourite.getServings(),
                            mCurrentFavourite.getTotalTime(),
                            mCurrentFavourite.getDetailedIngredientsList());
                    database.getReference(Constants.REVIEWS).child(mCurrentFavourite.getRecipeId()).child(getUserName()).setValue(reviewObject);
                    mReviewExists = true;
                    Toast.makeText(getApplication(), getString(R.string.review_upload_success_message), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplication(), getString(R.string.write_review_already_done_review_warning), Toast.LENGTH_LONG).show();
                }
            }
        };
        mBinding.writeSaveButton.setOnClickListener(reviewListener);

        View.OnClickListener starListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rate_star_1:
                        if (mStarRating == 1) {
                            mStarRating = 0;
                            setStars(0, 0, 0, 0, 0);
                        } else {
                            mStarRating = 1;
                            setStars(1, 0, 0, 0, 0);
                        }
                        break;
                    case R.id.rate_star_2:
                        mStarRating = 2;
                        setStars(1, 1, 0, 0, 0);
                        break;
                    case R.id.rate_star_3:
                        mStarRating = 3;
                        setStars(1, 1, 1, 0, 0);
                        break;
                    case R.id.rate_star_4:
                        mStarRating = 4;
                        setStars(1, 1, 1, 1, 0);
                        break;
                    default:
                        mStarRating = 5;
                        setStars(1, 1, 1, 1, 1);
                        break;
                }

            }
        };
        mBinding.rateStar1.setOnClickListener(starListener);
        mBinding.rateStar2.setOnClickListener(starListener);
        mBinding.rateStar3.setOnClickListener(starListener);
        mBinding.rateStar4.setOnClickListener(starListener);
        mBinding.rateStar5.setOnClickListener(starListener);
    }


    public void setStars(int star1, int star2, int star3, int star4, int star5) {
        if (star1 == 1) {
            mBinding.rateStar1.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            mBinding.rateStar1.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        if (star2 == 1) {
            mBinding.rateStar2.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            mBinding.rateStar2.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        if (star3 == 1) {
            mBinding.rateStar3.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            mBinding.rateStar3.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        if (star4 == 1) {
            mBinding.rateStar4.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            mBinding.rateStar4.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        if (star5 == 1) {
            mBinding.rateStar5.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            mBinding.rateStar5.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    public void checkForExistingReview() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot reviewSnapshot = null;
                if (dataSnapshot.getValue() == null) {
                    return;
                }
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey() != null && data.getKey().equals(mCurrentFavourite.getRecipeId())) {
                        reviewSnapshot = data;
                        break;
                    }
                }
                if (reviewSnapshot != null) {
                    for (DataSnapshot childData : reviewSnapshot.getChildren()) {
                        if (childData.getKey() != null && childData.getKey().equals(getUserName())) {
                            mReviewExists = true;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}