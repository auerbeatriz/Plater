package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class VerifyCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_verify_code);

        //Integrando a nova toolbar com a activity
        Toolbar toolbar = findViewById(R.id.tb_verifyCode);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText etNC1 = findViewById(R.id.et_numberCode1);
        EditText etNC2 = findViewById(R.id.et_numberCode2);
        EditText etNC3 = findViewById(R.id.et_numberCode3);
        EditText etNC4 = findViewById(R.id.et_numberCode4);

        etNC1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etNC1.getText().toString().length() == 1) {
                    etNC2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etNC2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etNC2.getText().toString().length() == 1) {
                    etNC3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etNC3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etNC3.getText().toString().length() == 1) {
                    etNC4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Intent ai = getIntent();
        String email = ai.getStringExtra("email");

        //Navigation between screens
        Button btnVerificarCodigo = findViewById(R.id.btn_enviarCodigo_fp);
        btnVerificarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                String code = etNC1.getText().toString() + etNC2.getText().toString() + etNC3.getText().toString() + etNC4.getText().toString();

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "recuperacao_validarCodigo.php", "POST", "UTF-8");
                        httpRequest.addParam("email", email);
                        httpRequest.addParam("code", code);

                        try {
                            InputStream is = httpRequest.execute();
                            String result = Util.inputStream2String(is, "UTF-8");
                            httpRequest.finish();

                            JSONObject jsonObject = new JSONObject(result);
                            final int success = jsonObject.getInt("success");
                            if(success == 1) {
                                Intent i = new Intent(VerifyCodeActivity.this, ChangePasswordActivity.class);
                                i.putExtra("email", email);
                                startActivity(i);
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VerifyCodeActivity.this, "Algo deu errado. Tente novamente.", Toast.LENGTH_LONG).show();
                                        v.setEnabled(false);
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        TextView tvResendCode = findViewById(R.id.tvResendCode);
        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VerifyCodeActivity.this, "Novo código enviado.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VerifyCodeActivity.this, "Não foi possível concluir esse pedido.", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(VerifyCodeActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}