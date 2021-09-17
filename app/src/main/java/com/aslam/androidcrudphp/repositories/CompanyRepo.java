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
import com.aslam.androidcrudphp.models.CompanyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CompanyRepo {

    private MutableLiveData<List<CompanyItem>> mutableCategoryList;

    public LiveData<List<CompanyItem>> getCompanies() {
        if (mutableCategoryList == null) {
            mutableCategoryList = new MutableLiveData<>();
            loadCompanies();
        }
        return mutableCategoryList;
    }

    private void loadCompanies() {
        List<CompanyItem> companyList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.1.100/aarot_mela/companies_all.php";

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

                                    String id = object.getString("cmp_id");
                                    String name = object.getString("cmp_name");
                                    String imageUrl = object.getString("cmp_image_url");

                                    companyList.add(new CompanyItem(id, name, imageUrl));
                                    mutableCategoryList.setValue(companyList);
                                }
                            }

                        }catch (JSONException jsonException){
                            jsonException.printStackTrace();
                        }

                        //Log.d("companyRepo: ",response);

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
