package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.plater.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_forgot_password);

        //Integrando a nova toolbar com a activity
        Toolbar toolbar = findViewById(R.id.tb_forgotPassword);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Navigation between screens
        Button btnEnviarEmail = findViewById(R.id.btn_confirmPassword);
        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                startActivity(i);
            }
        });

    }
}