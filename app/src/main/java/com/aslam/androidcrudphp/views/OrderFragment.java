package com.aslam.androidcrudphp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aslam.androidcrudphp.R;
import com.aslam.androidcrudphp.databinding.FragmentOrderBinding;
import com.aslam.androidcrudphp.models.CartItem;
import com.aslam.androidcrudphp.viewmodels.ShopViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OrderFragment extends Fragment {

    NavController navController;
    FragmentOrderBinding fragmentOrderBinding;
    ShopViewModel shopViewModel;
    FirebaseAuth mAuth;
    String  shopAddress,deliverymobile;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_order, container, false);
        fragmentOrderBinding = FragmentOrderBinding.inflate(inflater, container, false);
        return fragmentOrderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);

        checkShopAddress();

        fragmentOrderBinding.continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shopViewModel.resetCart();
                //navController.navigate(R.id.action_orderFragment_to_shopFragment);
                //Toast.makeText(getContext(),"Order being proccessed...",Toast.LENGTH_SHORT).show();
                shopAddress = fragmentOrderBinding.shopAddressEditText.getText().toString();
                deliverymobile = fragmentOrderBinding.deliveryPhoneEditText.getText().toString();
                Log.d("final userShopAddress: ", shopAddress);
                Log.d("final deliveryMobile: ", deliverymobile);

                if(shopAddress.isEmpty()){
                    Toast.makeText(getContext(),"Please enter your shop address.",Toast.LENGTH_SHORT).show();
                }else if(deliverymobile.isEmpty()){
                    Toast.makeText(getContext(),"Please enter your mobile number.",Toast.LENGTH_SHORT).show();
                }else{
                    makeFinalOrderComplete();
                }
            }
        });
    }

    private void makeFinalOrderComplete() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.0.105/aarot_mela/order_request.php";

        FirebaseUser user = mAuth.getCurrentUser();
        String userEmail = user.getEmail();
        List<CartItem> cartItems = shopViewModel.getCart().getValue();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                shopViewModel.resetCart();
                                navController.navigate(R.id.action_orderFragment_to_shopFragment);
                                Toast.makeText(getContext(),"Order has been placed successfully.",Toast.LENGTH_SHORT).show();
                            }else if(success.equals("0")){
                                String error = jsonObject.getString("error");
                                Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException jsonException){
                            jsonException.printStackTrace();
                        }

                        Log.d("Data: ",response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("response: ",error.getMessage());
                String message = null; // error message, show it in toast or dialog, whatever you want
                if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    message = "Cannot connect to Internet";
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again later";
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                }  else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again later";
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                }

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                String cartProducts = new Gson().toJson(cartItems);
                String cartTotalPrice = shopViewModel.getTotalPrice().toString();
                /*for(int i=0;i<cartItems.size();i++){
                    ArrayList<String> product = new ArrayList<>();
                    params.put("cartItems", cartItems.get(i).getProduct().getName());
                    //System.out.println("index "+i+" : id:"+cartItems.get(i).getProduct().getId()+" name: "+ cartItems.get(i).getProduct().getName()+" Quantity"+cartItems.get(i).getQuantity());
                }*/

                String userEmail = user.getEmail();
                params.put("email",userEmail);
                params.put("shippingAddress",fragmentOrderBinding.shopAddressEditText.getText().toString());
                params.put("shippingPhone",fragmentOrderBinding.deliveryPhoneEditText.getText().toString());
                params.put("cartItems",cartProducts);
                params.put("cartTotalPrice",cartTotalPrice);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

/*
    String userEmail = user.getEmail();
    List<CartItem> cartItems = shopViewModel.getCart().getValue();
                for(int i=0;i<cartItems.size();i++){
        ArrayList<String> product = new ArrayList<>();
        //System.out.println("index "+i+" : id:"+cartItems.get(i).getProduct().getId()+" name: "+ cartItems.get(i).getProduct().getName()+" Quantity"+cartItems.get(i).getQuantity());
    }*/
    private void checkShopAddress() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.0.105/aarot_mela/user_check.php";

        FirebaseUser user = mAuth.getCurrentUser();

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
                                String userShopAddress = jsonObject.getString("user_location");
                                String userPhone = jsonObject.getString("user_phone");

                                if(userShopAddress.equals("")){

                                }else{
                                    fragmentOrderBinding.shopAddressEditText.setText(userShopAddress);
                                    //Log.d("set userShopAddress: ", userShopAddress);
                                }

                                if(userPhone.equals("")){

                                }else{
                                    fragmentOrderBinding.deliveryPhoneEditText.setText(userPhone);
                                    //Log.d("set userPhone: ", userPhone);
                                }
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

                String userEmail = user.getEmail();
                Log.d("userEmail: ", userEmail);
                params.put("email",userEmail);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}