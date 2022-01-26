package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plater.R;
import com.example.plater.utils.Config;
import com.example.plater.utils.HttpRequest;
import com.example.plater.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                v.setEnabled(false);

                EditText etSendEmail = findViewById(R.id.etSendEmail);
                String email = etSendEmail.getText().toString();
                if(!email.isEmpty()) {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "recuperacao_enviarEmail.php", "POST", "UTF-8");
                            httpRequest.addParam("email", email);

                            try {
                                InputStream is = httpRequest.execute();
                                String result = Util.inputStream2String(is, "UTF-8");
                                httpRequest.finish();

                                JSONObject jsonObject = new JSONObject(result);
                                final int success = jsonObject.getInt("success");
                                if(success == 1) {
                                    Intent i = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                                    i.putExtra("email", email);
                                    startActivity(i);
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TextView tvNoRecords = findViewById(R.id.tvNoRecords);
                                            tvNoRecords.setVisibility(View.VISIBLE);
                                            v.setEnabled(true);
                                        }
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else {
                    v.setEnabled(true);
                    Toast.makeText(ForgotPasswordActivity.this, "Informe o email associado a conta", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}