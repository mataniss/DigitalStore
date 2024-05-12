package com.matan.digitalstore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

import java.io.IOException;

public class Login extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;

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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(usernameField.getText());
                String password = String.valueOf(passwordField.getText());
                PerformLogin performLogin = new PerformLogin(username,password);
                performLogin.execute();

            }
        });
    }

    public class PerformLogin extends AsyncTask< Integer, Integer, String > {
        private  String username;
        private String password;

        public PerformLogin(String username, String password) {
            this.username = username;
            this.password = password;
        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }

        @Override
        protected String doInBackground(Integer...integers) {
            try {
                boolean result = HttpUtil.loginRequest(username,password);
                System.out.println("Successful Login.");
            } catch (IOException | JSONException e) {
                String message = "Login failed " + e.toString();
                System.err.println(message);
//                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
            return null;
        }
//
//        @Override
//        protected void onProgressUpdate(Integer...values) {
//            super.onProgressUpdate(values);
//
//
//        }

//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//
//        }
    }

}