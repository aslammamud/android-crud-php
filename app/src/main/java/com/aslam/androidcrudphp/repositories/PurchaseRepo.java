package com.aslam.androidcrudphp.repositories;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aslam.androidcrudphp.R;
import com.aslam.androidcrudphp.databinding.PurchaseRowBinding;
import com.aslam.androidcrudphp.models.CartItem;
import com.aslam.androidcrudphp.models.PurchaseItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PurchaseRepo{

    static private MutableLiveData<List<PurchaseItem>> mutablePurchaseList;

    public LiveData<List<PurchaseItem>> getPurchaseHistories() {
        if (mutablePurchaseList == null) {
            mutablePurchaseList = new MutableLiveData<>();
            loadPurchaseHistories();
        }
        return mutablePurchaseList;
    }


    private void loadPurchaseHistories() {
        List<PurchaseItem> purchaseList = new ArrayList<>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        System.out.println(user.getEmail());

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.0.105/aarot_mela/shipment_all.php";

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

                                    String id = object.getString("ship_id");
                                    String token = object.getString("ship_order_token");
                                    String price = object.getString("ship_cost");
                                    String status = object.getString("ship_status");
                                    String destroyed = object.getString("ship_destroyed");
                                    String date = object.getString("ship_process_date");

                                    purchaseList.add(new PurchaseItem(id, token, price, Boolean.parseBoolean(status), Boolean.parseBoolean(destroyed), date));
                                    mutablePurchaseList.setValue(purchaseList);
                                }
                            }

                        }catch (JSONException jsonException){
                            jsonException.printStackTrace();
                        }

                        Log.d("purchaseHistoryRepo: ",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response: ",error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("email",user.getEmail());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public static void requestCancelOrderFromPurchase(PurchaseItem purchaseItem){
        System.out.println("shipment id : "+purchaseItem.getId());



        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.0.105/aarot_mela/shipment_req_cancel.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            //System.out.println(jsonObject);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                if (mutablePurchaseList.getValue() == null) {
                                    return;
                                }
                                List<PurchaseItem> purchaseItemList = new ArrayList<>(mutablePurchaseList.getValue());
                                purchaseItemList.remove(purchaseItem);
                                mutablePurchaseList.setValue(purchaseItemList);

                                Toast.makeText(getApplicationContext(),"Order Cancelled",Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException jsonException){
                            jsonException.printStackTrace();
                        }

                        //Log.d("Data: ",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response: ",error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                params.put("ship_id",purchaseItem.getId());

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void reloadPurchaseHistory(){
        List<PurchaseItem> purchaseItemList = new ArrayList<>(mutablePurchaseList.getValue());
        loadPurchaseHistories();
        mutablePurchaseList.setValue(purchaseItemList);
    }
}
