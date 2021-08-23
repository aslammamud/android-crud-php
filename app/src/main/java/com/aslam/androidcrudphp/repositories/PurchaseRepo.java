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
import com.aslam.androidcrudphp.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PurchaseRepo {

    private MutableLiveData<List<Product>> mutableProductList;

    public LiveData<List<Product>> getProducts() {
        if (mutableProductList == null) {
            mutableProductList = new MutableLiveData<>();
            loadProducts();
        }
        return mutableProductList;
    }

    private void loadProducts() {
        List<Product> productList = new ArrayList<>();
        /*productList.add(new Product(UUID.randomUUID().toString(), "iMac 21", 1299, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fimac21.jpeg?alt=media&token=e1cf1537-ab30-44a3-91f1-4d6284e79540" ));
        productList.add(new Product(UUID.randomUUID().toString(), "iPad Air", 799, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fipadair.jpeg?alt=media&token=da387155-bd8f-4343-954b-e46da7d252ae"));
        productList.add(new Product(UUID.randomUUID().toString(), "iPad Pro", 999, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fipadpro.jpeg?alt=media&token=5d433343-f3b3-43eb-8bf2-5298eb5bf11c"));
        productList.add(new Product(UUID.randomUUID().toString(), "iPhone 11", 699, false, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fiphone11.jpeg?alt=media&token=c6874af2-c81e-48eb-96e9-2f1f3fad617f"));
        productList.add(new Product(UUID.randomUUID().toString(), "iPhone 11 Pro", 999, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fiphone11pro.jpg?alt=media&token=c4547c4f-7a46-483d-80e5-8f1a93d96a03"));
        productList.add(new Product(UUID.randomUUID().toString(), "iPhone 11 Pro Max", 1099, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fiphone11promax.png?alt=media&token=109a89bd-e52b-4b76-91d4-5175aa516a23"));
        productList.add(new Product(UUID.randomUUID().toString(), "iPhone SE", 399, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fiphonese.jpeg?alt=media&token=8a3a144d-0cd8-4f6d-94cb-0d81634ea5d0"));
        productList.add(new Product(UUID.randomUUID().toString(), "MacBook Air", 999, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fmacbookair.jpeg?alt=media&token=aae96a4a-e86a-4a15-825a-3da9851330c8"));
        productList.add(new Product(UUID.randomUUID().toString(), "MacBook Pro 13", 1299, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fmbp13touch.jpeg?alt=media&token=88c2bf8e-e72d-4243-a9ab-4cc32e3aff18"));
        productList.add(new Product(UUID.randomUUID().toString(), "MacBook Pro 16", 2399, true, "https://firebasestorage.googleapis.com/v0/b/notes-16738.appspot.com/o/products%2Fmbp16touch.jpeg?alt=media&token=24498b7f-09b8-42ea-9edb-1bad649902d4"));*/

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.0.105/aarot_mela/products_all.php";

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

                                    String id = object.getString("product_id");
                                    String name = object.getString("product_name");
                                    double price = Double.parseDouble(object.getString("product_price"));
                                    String imageUrl = object.getString("product_image");
                                    String isAvailable = object.getString("product_status");

                                    if(isAvailable.equals("1")){
                                        productList.add(new Product(id, name, price, true, imageUrl));
                                    }else{
                                        productList.add(new Product(id, name, price, false, imageUrl));
                                    }
                                    mutableProductList.setValue(productList);
                                }
                            }

                        }catch (JSONException jsonException){
                            jsonException.printStackTrace();
                        }

                        Log.d("ShopRepo: ",response);

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
