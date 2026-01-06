package com.example.healthcarecompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText editUserName, editPassword;
    Button btn;
    TextView tvRegister;


    FirebaseAuth auth;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUserName = findViewById(R.id.userinput);
        editPassword = findViewById(R.id.password);
        btn = findViewById(R.id.loginbutton);
        tvRegister = findViewById(R.id.register);


        auth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        btn.setOnClickListener(view -> {
            String email = editUserName.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            String error = task.getException() != null ?
                                    task.getException().getMessage() : "Login failed";
                            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });


    }
}
