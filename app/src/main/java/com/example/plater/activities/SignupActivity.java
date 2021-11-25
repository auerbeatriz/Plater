package com.example.plater.activities;

import androidx.appcompat.app.AppCompatActivity;

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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  activity fica em "tela cheia"
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  exibindo layout
        setContentView(R.layout.activity_signup);

        Button btnCadastrar = findViewById(R.id.btn_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                //  obtendo dados passados para cadastro
                EditText etNome = findViewById(R.id.et_nome);
                final String nome = etNome.getText().toString();
                if(nome.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Nome não informado", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                EditText etUsername = findViewById(R.id.et_nome_usuario);
                final String username = etUsername.getText().toString();
                if(username.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Username não informado", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                EditText etEmail = findViewById(R.id.et_email);
                final String email = etEmail.getText().toString();
                if(email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Email não informado", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                EditText etSenha = findViewById(R.id.etSenha);
                final String senha = etSenha.getText().toString();
                if(senha.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Senha não informado", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //  enviando dados para login
                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "cadastro_usuario.php", "POST", "UTF-8");
                        httpRequest.addParam("nome", nome);
                        httpRequest.addParam("username", username);
                        httpRequest.addParam("email", email);
                        httpRequest.addParam("senha", senha);

                        try {
                            // Tenta fazer a insercao de um usuario no banco de dados
                            InputStream is = httpRequest.execute();
                            String result = Util.inputStream2String(is, "UTF-8");
                            httpRequest.finish();

                            Log.d("HTTP_REQUEST_RESULT", result);

                            JSONObject jsonObject = new JSONObject(result);
                            final int success = jsonObject.getInt("success");
                            if(success == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignupActivity.this, "Novo usuario registrado com sucesso", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            } else {
                                final String erro = jsonObject.getString("error");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignupActivity.this, erro, Toast.LENGTH_LONG).show();
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
        });

        //Navigation between screens
        TextView tvEntrar = findViewById(R.id.tv_link_entrar);
        tvEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}