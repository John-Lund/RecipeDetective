package com.example.android.recipedetective.repo;

import com.example.android.recipedetective.R;
import com.example.android.recipedetective.model.DetailsObject;
import com.example.android.recipedetective.model.SearchObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class Utilities {
    // todo: insert app id and app key in the positions indicated below
    private static final String RECIPE_SEARCH_BASE_URL = "http://api.yummly.com/v1/api/recipes&[insert app id and key here]requirePictures=true";
    private static final String RECIPE_DETAILS_BASE_URL = "http://api.yummly.com/v1/api/recipe/";
    private static final String RECIPE_DETAILS_SUFFIX = "?[insert app id and key here]";

    public static DetailsObject getRecipeDetails(String recipeId) {
        URL url = createDetailsUrl(recipeId);
        String jsonResponce = "";
        try {
            jsonResponce = loadJSON(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonResponce != null && !jsonResponce.isEmpty()) {
            return extractDetailsObject(jsonResponce, recipeId);
        } else {
            return null;
        }
    }

    private static URL createDetailsUrl(String recipeId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RECIPE_DETAILS_BASE_URL).append(recipeId).append(RECIPE_DETAILS_SUFFIX);
        String urlString = stringBuilder.toString();
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }

    private static DetailsObject extractDetailsObject(String jsonResponse, String recipeId) {
        DetailsObject recipeDetails = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String recipeName = jsonObject.optString("name", "unknown");
            JSONObject sourceData = jsonObject.optJSONObject("source");
            String sourceName = "";
            String sourceUrl = "";
            if (sourceData != null) {
                sourceName = sourceData.optString("sourceDisplayName", "unknown");
                sourceUrl = sourceData.optString("sourceRecipeUrl", "unknown");
            }
            String servings = jsonObject.optString("yield", "-");
            servings = servings.replace("Yield:", "").replace("servings", "").replace("serves", "").replace("yield:", "");
            String preparationTime = jsonObject.optString("totalTime", "-");
            JSONArray ingredientsArray = jsonObject.optJSONArray("ingredientLines");
            String detailedIngredients = "";
            if (ingredientsArray != null && ingredientsArray.length() > 0) {
                detailedIngredients = ingredientsArray.join("\n");
                detailedIngredients = detailedIngredients.replace("\\/", "/");
                detailedIngredients = detailedIngredients.replace("\"", "");
            }
            recipeDetails = new DetailsObject(
                    recipeId,
                    recipeName,
                    sourceName,
                    sourceUrl,
                    servings,
                    preparationTime,
                    detailedIngredients);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeDetails;
    }


    public static List<SearchObject> getRecipes(String[] ingredients) {
        URL url = createSearchUrl(ingredients);
        String jsonResponse = "";
        try {
            jsonResponse = loadJSON(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            return extractSearchObjects(jsonResponse);
        } else {
            return null;
        }
    }

    private static List<SearchObject> extractSearchObjects(String jsonResponse) {
        ArrayList<SearchObject> recipes = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.optJSONArray("matches");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject matchObject = jsonArray.getJSONObject(i);
                    // getting the image string
                    String imageUrlString = "unknown";
                    JSONArray imageArray = matchObject.optJSONArray("smallImageUrls");
                    if (imageArray != null) {
                        imageUrlString = imageArray.optString(0, "unknown");
                        imageUrlString = imageUrlString.replace("=s90", "=s800");
                    }
                    // getting source
                    String sourceString = matchObject.optString("sourceDisplayName", "unknown");
                    // getting ingredients list
                    JSONArray ingredientsJsonArray = matchObject.optJSONArray("ingredients");
                    String ingredientsList = "unknown";
                    if (ingredientsJsonArray != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int k = 0; k < ingredientsJsonArray.length(); k++) {
                            sb.append(ingredientsJsonArray.get(k)).append("\n");
                        }
                        ingredientsList = sb.toString().replaceAll("^\"|\"$", "");
                    }
                    // getting recipe name
                    String recipeName = matchObject.optString("recipeName", "unknown");
                    // getting recipe id
                    String recipeId = matchObject.optString("id", "unknown");
                    // getting rating
                    int rating = matchObject.optInt("rating", 0);
                    // getting rating reference
                    int ratingReference;
                    // getting the right star-row drawable for the rating
                    switch (rating) {
                        case 0:
                            ratingReference = R.drawable.nostarsnew;
                            break;
                        case 1:
                            ratingReference = R.drawable.onestarnew;
                            break;
                        case 2:
                            ratingReference = R.drawable.twostarsnew;
                            break;
                        case 3:
                            ratingReference = R.drawable.threestarsnew;
                            break;
                        case 4:
                            ratingReference = R.drawable.fourstarsnew;
                            break;
                        default:
                            ratingReference = R.drawable.fivestarsnew;
                            break;
                    }
                    // creating search object and adding it to the array list
                    SearchObject searchObject = new SearchObject(imageUrlString, sourceString,
                            ingredientsList, recipeName, recipeId, rating, ratingReference);
                    recipes.add(searchObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private static URL createSearchUrl(String[] ingredients) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RECIPE_SEARCH_BASE_URL);
        for (String ingredient : ingredients) {
            stringBuilder.append("&allowedIngredient[]=").append(ingredient);
        }
        String urlString = stringBuilder.toString();
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }

    private static String loadJSON(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        if (url == null) {
            return null;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = convertStream(inputStream);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String convertStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String output = reader.readLine();
            while (output != null) {
                builder.append(output);
                output = reader.readLine();
            }
        }
        return builder.toString();
    }
}