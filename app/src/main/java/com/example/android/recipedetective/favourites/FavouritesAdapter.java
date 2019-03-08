package com.example.android.recipedetective.favourites;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.recipedetective.R;
import com.example.android.recipedetective.database.FavouritesObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private Activity mActivity;
    private ItemClickListener mItemClickListener;
    private List<FavouritesObject> mFavouritesObjects;

    public FavouritesAdapter(Activity activity, ItemClickListener itemClickListener) {
        mActivity = activity;
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int recipeIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourites_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.with(mActivity)
                .load(mFavouritesObjects.get(i).getImageUrl())
                .into(viewHolder.mFavouritesItemImage);
        if (i % 2 == 0) {
            viewHolder.mLightRecipeTitleBackground.setVisibility(View.VISIBLE);
            viewHolder.mLightRecipeTitleTextView.setVisibility(View.VISIBLE);
            viewHolder.mDecorationLightImageView.setVisibility(View.VISIBLE);
            viewHolder.mDarkRecipeTitleBackground.setVisibility(View.GONE);
            viewHolder.mDarkRecipeTitleTextView.setVisibility(View.GONE);
            viewHolder.mDecorationDarkImageView.setVisibility(View.GONE);
            viewHolder.mLightRecipeTitleTextView.setText(mFavouritesObjects.get(i).getRecipeName());
        } else {
            viewHolder.mLightRecipeTitleBackground.setVisibility(View.GONE);
            viewHolder.mLightRecipeTitleTextView.setVisibility(View.GONE);
            viewHolder.mDecorationLightImageView.setVisibility(View.GONE);
            viewHolder.mDarkRecipeTitleBackground.setVisibility(View.VISIBLE);
            viewHolder.mDarkRecipeTitleTextView.setVisibility(View.VISIBLE);
            viewHolder.mDecorationDarkImageView.setVisibility(View.VISIBLE);
            viewHolder.mDarkRecipeTitleTextView.setText(mFavouritesObjects.get(i).getRecipeName());
        }
    }

    @Override
    public int getItemCount() {
        if (mFavouritesObjects != null && mFavouritesObjects.size() > 0) {
            return mFavouritesObjects.size();
        } else {
            return 0;
        }
    }

    public void setData(List<FavouritesObject> data) {
        mFavouritesObjects = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mFavouritesItemImage;
        private TextView mLightRecipeTitleBackground;
        private TextView mLightRecipeTitleTextView;
        private ImageView mDecorationLightImageView;
        private TextView mDarkRecipeTitleBackground;
        private TextView mDarkRecipeTitleTextView;
        private ImageView mDecorationDarkImageView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFavouritesItemImage = itemView.findViewById(R.id.favourites_item_image);
            mLightRecipeTitleBackground = itemView.findViewById(R.id.light_recipe_title_background);
            mLightRecipeTitleTextView = itemView.findViewById(R.id.light_recipe_title_text_view);
            mDecorationLightImageView = itemView.findViewById(R.id.decoration_light_image_view);
            mDarkRecipeTitleBackground = itemView.findViewById(R.id.dark_recipe_title_background);
            mDarkRecipeTitleTextView = itemView.findViewById(R.id.dark_recipe_title_text_view);
            mDecorationDarkImageView = itemView.findViewById(R.id.decoration_dark_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mItemClickListener.onItemClick(position);
        }
    }
}
