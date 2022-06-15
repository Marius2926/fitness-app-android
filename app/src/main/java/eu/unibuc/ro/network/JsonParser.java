package eu.unibuc.ro.network;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    public static ArrayList<Product> buildResponseFromJsonString(String json) {
        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            return getListOfProductsFromJson(jsonObj.getJSONArray("products"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static ArrayList<Product> getListOfProductsFromJson(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Product> alimentsList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Product product = getProductFromJson(jsonArray.getJSONObject(i));

            if (product != null) {
                alimentsList.add(product);
            }
        }
        return alimentsList;
    }

    private static Product getProductFromJson(JSONObject jsonObj) throws JSONException {
        if (jsonObj == null) {
            return null;
        }

        Product product = new Product();
        product.setName(jsonObj.getString("name"));
        product.setQuantity(jsonObj.getString("quantity"));
        product.setCaloriesNumber(Integer.valueOf(jsonObj.getString("caloriesNumber")));
        product.setNutrients(getNutrientsFromJson(jsonObj.getJSONObject("nutrients")));

        return product;
    }

    private static Nutrients getNutrientsFromJson(JSONObject jsonObj) throws JSONException {
        if (jsonObj == null) {
            return null;
        }

        Nutrients nutrients = new Nutrients();
        nutrients.setProteins(jsonObj.getString("protein"));
        nutrients.setCarbs(jsonObj.getString("carbs"));
        nutrients.setFats(jsonObj.getString("fat"));
        nutrients.setFiber(jsonObj.getString("fiber"));
        nutrients.setSugar(jsonObj.getString("sugar"));

        return nutrients;
    }


}
