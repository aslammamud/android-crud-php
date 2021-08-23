package com.aslam.androidcrudphp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aslam.androidcrudphp.R;
import com.aslam.androidcrudphp.views.HomeActivity;
import com.aslam.androidcrudphp.views.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.makeText;

public class GoogleAuthActivity extends MainActivity {

    private static final int RC_SIGN_IN = 101;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Google Signing In...");
        progressDialog.show();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressDialog.dismiss();
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(GoogleAuthActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        //Intent intent = new Intent(GoogleAuthActivity.this, HomeActivity.class);
        //startActivity(intent);

        if(user!=null){

            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://192.168.0.105/aarot_mela/user_insert.php";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //textView.setText("Response is: "+ response.substring(0,500));
                            if(response.equalsIgnoreCase("User profile created!")){
                                Log.d("response: ","User profile created!");
                            }else{
                                Log.d("response: ",response);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.getMessage() ,Toast.LENGTH_SHORT).show();
                    Log.d("response: ",error.getMessage());
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();

                    String userName = user.getDisplayName();
                    String userEmail = user.getEmail();
                    String userPhone = user.getPhoneNumber();

                    Log.d("credentials",userName+" "+userEmail+" "+userPhone);


                    params.put("name",userName);
                    params.put("email",userEmail);

                    //params.put("phone",userPhone);

                    return params;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

    }
}
