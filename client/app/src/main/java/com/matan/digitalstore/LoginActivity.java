package com.matan.digitalstore;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.matan.digitalstore.Utils.HttpUtil;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.signUpBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(usernameField.getText());
                String password = String.valueOf(passwordField.getText());
                PerformLogin performLogin = new PerformLogin(username,password);
                performLogin.execute();

            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: open signup activity
            }
        });
    }

    public class PerformLogin extends AsyncTask< Integer, Integer, Boolean > {
        private  String username;
        private String password;

        public PerformLogin(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Integer...integers) {
            try {
                boolean result = HttpUtil.loginRequest(username,password);
                if(result == true)
                {
                    System.out.println("Successful Login.");
                    return true;
                }
                else {
                    throw new Error("Login failed");
                }

            } catch (IOException | JSONException e) {
                String message = "Login failed " + e.toString();
                System.err.println(message);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            String message ;
            if(result == true){
                message = "Successful Login.";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), CatalogActivity.class);
                startActivity(i);
            }
            else {
                message = "Login failed";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

        }
    }

}