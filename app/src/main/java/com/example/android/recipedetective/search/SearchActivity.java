package com.example.android.recipedetective.search;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.databinding.ActivitySearchBinding;
import com.example.android.recipedetective.model.SearchObject;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.ItemClickListener {
    public static final String CHECK_TEXT = "check text";
    private ActivitySearchBinding mBinding;
    private SearchActivityViewModel mViewModel;
    private SearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setUpViewModel();
        setUpUi();
        freezeAppBar();
        if (savedInstanceState != null) {
            String check = savedInstanceState.getString(CHECK_TEXT, "xxx");
            mBinding.checkText.setText(check);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CHECK_TEXT, mBinding.checkText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void setUpViewModel() {
        mViewModel = ViewModelProviders.of(this).get(SearchActivityViewModel.class);
        mViewModel.getSearchResults().observe(this, new Observer<List<SearchObject>>() {
            @Override
            public void onChanged(@Nullable List<SearchObject> searchObjects) {
                if (mAdapter != null) {
                    mAdapter.setData(searchObjects);
                    if (searchObjects != null && searchObjects.size() == 0) {
                        mBinding.checkText.setText(getString(R.string.search_no_hits_warning));
                    }
                    if (searchObjects != null && searchObjects.size() > 0) {
                        String searchString = String.valueOf(mBinding.searchActivityEditText.getText());
                        setUpUi();
                        mBinding.searchActivityRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter.setData(searchObjects);
                        mBinding.searchActivityEditText.setText(searchString);
                        mBinding.checkText.setText(searchString);
                    }
                }
            }
        });
    }

    private void setUpUi() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        // setting up toolbar
        setSupportActionBar(mBinding.searchActivityToolbar);
        mBinding.searchActivityToolbar.setNavigationIcon(R.drawable.svg_baseline_arrow_back_ios___px);
        mBinding.searchActivityToolbar.setTitle("");
        mBinding.searchActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mBinding.searchActivityEditText.setHintTextColor(getResources().getColor(R.color.colorPrimaryLight));
        mBinding.searchActivityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // keeps "check text" view updated with user input
                if (!mBinding.searchActivityEditText.getText().toString().equals("")) {
                    mBinding.checkText.setText(String.valueOf(s));
                } else if (mBinding.searchActivityEditText.getText().toString().equals("")
                        && mBinding.checkText.getText().length() == 1) {
                    mBinding.checkText.setText(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBinding.searchActivityRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.search_recycler_column_count)));
        mAdapter = new SearchAdapter(this, this);
        mBinding.searchActivityRecyclerView.setAdapter(mAdapter);
        mBinding.searchActivityGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                if (internetIsOk()) {
                    mBinding.searchActivityRecyclerView.setVisibility(View.GONE);
                    String searchValidity = mViewModel.processSearchString(
                            mBinding.searchActivityEditText.getText().toString());
                    mBinding.checkText.setText(searchValidity);
                } else {
                    Toast.makeText(SearchActivity.this, getString(R.string.search_activity_internet_warning), Toast.LENGTH_SHORT).show();
                }

            }
        });
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

    // prevents coordinator layout from collapsing prematurely
    public void freezeAppBar() {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                mBinding.appBarLayout.getLayoutParams();
        layoutParams.setBehavior(new AppBarLayout.ScrollingViewBehavior(mBinding.appBarLayout.getContext(), null));
    }

    // closes keyboard
    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = this.getCurrentFocus();
        if (v != null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // adapter interface method for opening details activity with correct recipe data
    @Override
    public void onItemClick(int recipeIndex) {
        mViewModel.setCurrentSearchObject(recipeIndex);
        mViewModel.upDateCurrentDetailsObject();
        Intent intent = new Intent(this, SearchDetailsActivity.class);
        startActivity(intent);
        CustomIntent.customType(SearchActivity.this, "left-to-right");
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(SearchActivity.this, "right-to-left");
    }
}
