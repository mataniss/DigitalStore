package com.matan.digitalstore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.matan.digitalstore.Utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        emailEditText = findViewById(R.id.email);
        submitButton = findViewById(R.id.submit);

        submitButton.setOnClickListener(view -> {
            if(String.valueOf(passwordEditText.getText())
                    .equals(String.valueOf(confirmPasswordEditText.getText()))){
                SignUpAction signUpAction = new SignUpAction();
                signUpAction.execute();
            }
            else {
                Toast.makeText(getApplicationContext(),"Passwords don't match",Toast.LENGTH_LONG).show();
            }

        });
    }

    public class SignUpAction extends AsyncTask< Integer, Integer, Long> {

        @Override
        protected Long doInBackground(Integer...integers) {
            Long userID = null;
            String username = String.valueOf(usernameEditText.getText());
            String password = String.valueOf(passwordEditText.getText());
            String email = String.valueOf(emailEditText.getText());
            try {
                JSONObject json = new JSONObject();
                json.put("username",username);
                json.put("password", password);
                json.put("email", email);
                ResponseBody responseBody = HttpUtil.postRequest("users/signup",json,false);
                userID = Long.valueOf(responseBody.string());

            } catch (IOException | JSONException e) {
                System.err.println("An error occurred during signup.");
            }
            return userID;
        }

        @Override
        protected void onPostExecute(Long purchaseID) {
            if(purchaseID == null){
                String message = "An error occurred during signup.";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Sign up was successful.",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}