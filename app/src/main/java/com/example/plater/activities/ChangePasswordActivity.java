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

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_change_password);

        //Integrando a nova toolbar com a activity
        Toolbar toolbar = findViewById(R.id.tb_ChangePassword);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent ai = getIntent();
        String email = ai.getStringExtra("email");

        //Navigation between screens
        Button btnAlterarSenha = findViewById(R.id.btn_confirmPassword);
        btnAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNewPassword = findViewById(R.id.et_newPassword);
                EditText etConfirmNewPassword = findViewById(R.id.et_confirmNewPassword);

                final String senha = etNewPassword.getText().toString();
                final String confirmaSenha = etConfirmNewPassword.getText().toString();

                if(senha.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, "Senha não informado", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                if(senha.length() < 6) {
                    Toast.makeText(ChangePasswordActivity.this, "Senha muito curta. Insira pelo menos 6 caracteres.", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                if(!senha.equals(confirmaSenha)) {
                    Toast.makeText(ChangePasswordActivity.this, "As senhas informadas são diferentes.", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "/recuperacao_alterarSenha.php", "POST", "UTF-8");
                        httpRequest.addParam("email", email);
                        httpRequest.addParam("senha", senha);

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
                                        Toast.makeText(ChangePasswordActivity.this, "Senha alterada com sucesso", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
                            else {
                                final String message = jsonObject.getString("message");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
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