package com.example.android.recipedetective.search;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.model.SearchObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Activity mActivity;
    private ItemClickListener mItemClickListener;
    private List<SearchObject> mSearchObjects;

    public SearchAdapter(Activity activity, ItemClickListener itemClickListener) {
        mActivity = activity;
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int recipeIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_activity_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.with(mActivity)
                .load(mSearchObjects.get(i).getImagineUrlString())
                .into(viewHolder.mSearchItemImage);
        viewHolder.mSearchRecipeNameText.setText(mSearchObjects.get(i).getRecipeName());
        viewHolder.mSearchSourceText.setText(mSearchObjects.get(i).getSourceDisplayName());
        viewHolder.mSearchRatingImageView.setImageResource(mSearchObjects.get(i).getmRatingReference());
    }

    @Override
    public int getItemCount() {
        if (mSearchObjects != null && mSearchObjects.size() > 0) {
            return mSearchObjects.size();
        } else {
            return 0;
        }
    }

    public void setData(List<SearchObject> data) {
        mSearchObjects = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mSearchItemImage;
        private TextView mSearchSourceText;
        private TextView mSearchRecipeNameText;
        private ImageView mSearchRatingImageView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mSearchItemImage = itemView.findViewById(R.id.search_item_image);
            this.mSearchSourceText = itemView.findViewById(R.id.search_source_text);
            this.mSearchRecipeNameText = itemView.findViewById(R.id.search_recipe_name_text);
            this.mSearchRatingImageView = itemView.findViewById(R.id.search_rating_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mItemClickListener.onItemClick(position);
        }
    }
}
