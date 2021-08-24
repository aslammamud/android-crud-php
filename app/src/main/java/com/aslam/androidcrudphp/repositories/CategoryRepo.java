package com.aslam.androidcrudphp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aslam.androidcrudphp.models.CategoryItem;
import com.aslam.androidcrudphp.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryRepo {

    private MutableLiveData<List<CategoryItem>> mutableCategoryList;

    public LiveData<List<CategoryItem>> getCategories() {
        if (mutableCategoryList == null) {
            mutableCategoryList = new MutableLiveData<>();
            loadCategories();
        }
        return mutableCategoryList;
    }

    private void loadCategories() {
        List<CategoryItem> categoryList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.0.105/aarot_mela/categories_all.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //productList.clear();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(success.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("category_id");
                                    String name = object.getString("category_name");
                                    String imageUrl = object.getString("category_image");
                                    String isAvailable = object.getString("category_status");

                                    if(isAvailable.equals("1")){
                                        categoryList.add(new CategoryItem(id, name, imageUrl, true));
                                    }else{
                                        categoryList.add(new CategoryItem(id, name, imageUrl, false));
                                    }
                                    mutableCategoryList.setValue(categoryList);
                                }
                            }

                        }catch (JSONException jsonException){
                            jsonException.printStackTrace();
                        }

                        Log.d("categoryRepo: ",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response: ",error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
