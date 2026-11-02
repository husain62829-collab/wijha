package com.example.wijha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity { // <--- لاحظي أن الاسم Username هنا مطابق لاسم الملف

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "الرجاء إدخال اسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();

                    // الانتقال إلى MainActivity
                    Intent intent = new Intent(MainActivity.this, BookingActivity.class);
                    intent.putExtra("USERNAME_KEY", username);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}