package com.example.android.recipedetective.reviews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.model.ReviewObject;
import com.example.android.recipedetective.repo.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Activity mActivity;
    private ItemClickListener mItemClickListener;

    public ReviewAdapter(Activity activity, ItemClickListener itemClickListener) {
        mActivity = activity;
        mItemClickListener = itemClickListener;
    }

    private List<ReviewObject> mReviewObjects;

    public interface ItemClickListener {
        void onItemClick(int recipeIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.read_reviews_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mRecipeTitle.setText(mReviewObjects.get(i).getRecipeTitle());
        viewHolder.mReviewAuthorName.setText(mReviewObjects.get(i).getAuthor());
        viewHolder.mReviewBody.setText(mReviewObjects.get(i).getReviewText());
        viewHolder.mReviewStarsImage.setImageResource(mReviewObjects.get(i).getReviewStarsImageId());
        if (mReviewObjects.get(i).getRecipeIsFavourite()) {
            viewHolder.mAddFavouriteImageButton.setVisibility(View.GONE);
            viewHolder.mBookmarkImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mAddFavouriteImageButton.setVisibility(View.VISIBLE);
            viewHolder.mBookmarkImageView.setVisibility(View.GONE);
        }
        if (mReviewObjects.get(i).getHeaderValue().equals(Constants.HEADERLESS)) {
            viewHolder.mHeaderLayout.setVisibility(View.GONE);
        } else {
            viewHolder.mHeaderLayout.setVisibility(View.VISIBLE);
            Picasso.with(mActivity)
                    .load(mReviewObjects.get(i).getImageUrl())
                    .into(viewHolder.mRecipeImage);
        }
        if (mReviewObjects.get(i).getFooterValue().equals(Constants.FOOTERLESS)) {
            viewHolder.mReviewLowerDecoration.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mReviewLowerDecoration.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mReviewObjects != null && mReviewObjects.size() > 0) {
            return mReviewObjects.size();
        } else {
            return 0;
        }
    }

    public void setData(List<ReviewObject> data) {
        mReviewObjects = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mRecipeTitle;
        private ImageView mRecipeImage;
        private TextView mReviewAuthorName;
        private ImageView mReviewStarsImage;
        private TextView mReviewBody;
        private ImageView mReviewLowerDecoration;
        private ImageButton mAddFavouriteImageButton;
        private ImageView mBookmarkImageView;
        private ConstraintLayout mHeaderLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mRecipeTitle = itemView.findViewById(R.id.review_recipe_title_text);
            this.mRecipeImage = itemView.findViewById(R.id.review_recipe_image);
            this.mReviewAuthorName = itemView.findViewById(R.id.review_author_name);
            this.mReviewStarsImage = itemView.findViewById(R.id.review_stars_image);
            this.mReviewBody = itemView.findViewById(R.id.review_body);
            this.mReviewLowerDecoration = itemView.findViewById(R.id.review_lower_decoration);
            this.mAddFavouriteImageButton = itemView.findViewById(R.id.add_favourite_image_button);
            this.mBookmarkImageView = itemView.findViewById(R.id.bookmark_image_view);
            this.mHeaderLayout = itemView.findViewById(R.id.reviews_header_layout);
            mAddFavouriteImageButton.setOnClickListener(this);
            mAddFavouriteImageButton.setTag(mBookmarkImageView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mReviewObjects.get(position).setRecipeIsFavourite(true);
            v.setVisibility(View.GONE);
            ((ImageView) v.getTag()).setVisibility(View.VISIBLE);
            mItemClickListener.onItemClick(position);
        }
    }
}
