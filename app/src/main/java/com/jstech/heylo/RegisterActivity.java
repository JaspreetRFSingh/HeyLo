package com.jstech.heylo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle(R.string.app_name);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        username = (EditText)findViewById(R.id.usernameReg);
        password = (EditText)findViewById(R.id.passwordReg);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("This field can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("This field can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("only alphabet or number allowed");
                }
                else if(user.length()<5){
                    username.setError("at least 5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("at least 5 characters long");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://demoprojects-1b91f.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://demoprojects-1b91f.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                                        username.setText("");
                                        password.setText("");
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Username already exists!", Toast.LENGTH_LONG).show();
                                        username.setText("");
                                        password.setText("");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
                    rQueue.add(request);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}