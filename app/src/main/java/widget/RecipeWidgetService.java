package widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.repo.Repository;

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeDetectiveWidgetFactory(getApplicationContext());
    }

    class RecipeDetectiveWidgetFactory implements RemoteViewsFactory {
        private Context mContext;
        private Repository mRepository;

        RecipeDetectiveWidgetFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {
            mRepository = Repository.getRepository();
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews view = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_list_item);
            if (mRepository.getCurrentWidgetDataObject() != null) {
                view.setTextViewText(R.id.recipe_title_text_view, mRepository.getCurrentWidgetDataObject().getRecipeTitle());
                view.setTextViewText(R.id.ingredients_list_text_view, mRepository.getCurrentWidgetDataObject().getBasicIngredientsList());
            } else {
                view.setTextViewText(R.id.recipe_title_text_view, getString(R.string.initial_widget_message_to_user));
                view.setTextViewText(R.id.ingredients_list_text_view, "");
            }
            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
