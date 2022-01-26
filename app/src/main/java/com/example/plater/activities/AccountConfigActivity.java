package com.example.plater.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
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

public class AccountConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_account_config);

        Toolbar toolbar = findViewById(R.id.tbAccountConfig);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvNome = findViewById(R.id.tvCurrentName);
        tvNome.setText(Config.getName(AccountConfigActivity.this));

        TextView tvEmail = findViewById(R.id.tvCurrentEmail);
        tvEmail.setText(Config.getLogin(AccountConfigActivity.this));

        Button btnChangeName = findViewById(R.id.btnChangeName);
        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etChangeName = findViewById(R.id.etChangeName);
                if(!etChangeName.getText().toString().isEmpty()) {
                    etChangeName.setEnabled(false);
                    String nome = etChangeName.getText().toString();

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "/atualiza_dados_usuario.php", "POST", "UTF-8");
                            httpRequest.setBasicAuth(Config.getLogin(AccountConfigActivity.this), Config.getPassword(AccountConfigActivity.this));
                            httpRequest.addParam("username", Config.getUsername(AccountConfigActivity.this));
                            httpRequest.addParam("opcao", "nome");
                            httpRequest.addParam("valor", nome);

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
                                            Config.setName(AccountConfigActivity.this, nome);
                                            tvNome.setText(nome);
                                            etChangeName.setEnabled(true);
                                            Toast.makeText(AccountConfigActivity.this, "Nome alterado com sucesso", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else {
                                    final String message = jsonObject.getString("message");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            etChangeName.setEnabled(true);
                                            Toast.makeText(AccountConfigActivity.this, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountConfigActivity.this, "Campo vazio.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnChangeEmail = findViewById(R.id.btnChangeEmail);
        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etChangeEmail = findViewById(R.id.etChangeEmail);
                if(!etChangeEmail.getText().toString().isEmpty()) {
                    etChangeEmail.setEnabled(false);
                    String email = etChangeEmail.getText().toString();

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "/atualiza_dados_usuario.php", "POST", "UTF-8");
                            httpRequest.setBasicAuth(Config.getLogin(AccountConfigActivity.this), Config.getPassword(AccountConfigActivity.this));
                            httpRequest.addParam("username", Config.getUsername(AccountConfigActivity.this));
                            httpRequest.addParam("opcao", "email");
                            httpRequest.addParam("valor", email);

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
                                            Config.setLogin(AccountConfigActivity.this, email);
                                            tvEmail.setText(email);
                                            etChangeEmail.setText("");
                                            etChangeEmail.setEnabled(true);
                                            Toast.makeText(AccountConfigActivity.this, "Email alterado com sucesso", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else {
                                    final String message = jsonObject.getString("message");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            etChangeEmail.setEnabled(true);
                                            Toast.makeText(AccountConfigActivity.this, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountConfigActivity.this, "Campo vazio.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etChangePassword = findViewById(R.id.etChangePassword);
                EditText etConfirmChangePassword = findViewById(R.id.etConfirmChangePassword);

                etChangePassword.setEnabled(false);
                etConfirmChangePassword.setEnabled(false);

                final String senha = etChangePassword.getText().toString();
                final String confirmaSenha = etConfirmChangePassword.getText().toString();

                if(senha.isEmpty()) {
                    Toast.makeText(AccountConfigActivity.this, "Senha não informado", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                if(senha.length() < 6) {
                    Toast.makeText(AccountConfigActivity.this, "Senha muito curta. Insira pelo menos 6 caracteres.", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }
                if(!senha.equals(confirmaSenha)) {
                    Toast.makeText(AccountConfigActivity.this, "As senhas informadas são diferentes.", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                    return;
                }

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "/atualiza_dados_usuario.php", "POST", "UTF-8");
                        httpRequest.setBasicAuth(Config.getLogin(AccountConfigActivity.this), Config.getPassword(AccountConfigActivity.this));
                        httpRequest.addParam("username", Config.getUsername(AccountConfigActivity.this));
                        httpRequest.addParam("opcao", "senha");
                        httpRequest.addParam("valor", senha);

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
                                        Config.setPassword(AccountConfigActivity.this, senha);
                                        etChangePassword.setText("");
                                        etConfirmChangePassword.setText("");

                                        etChangePassword.setEnabled(true);
                                        etConfirmChangePassword.setEnabled(true);

                                        Toast.makeText(AccountConfigActivity.this, "Senha alterada com sucesso", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                final String message = jsonObject.getString("message");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        etChangePassword.setEnabled(true);
                                        etConfirmChangePassword.setEnabled(true);
                                        Toast.makeText(AccountConfigActivity.this, message, Toast.LENGTH_LONG).show();
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

        TextView tvLogout = findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.setLogin(AccountConfigActivity.this, "");
                Config.setPassword(AccountConfigActivity.this, "");
                Config.setUsername(AccountConfigActivity.this, "");
                Config.setName(AccountConfigActivity.this, "");

                Intent i = new Intent(AccountConfigActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        TextView tvDeleteAccount = findViewById(R.id.tvDeleteAccount);
        tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountConfigActivity.this);
                builder.setMessage(R.string.ad_delete_account)
                        .setPositiveButton(R.string.ad_sim, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // DELETE THE ACCOUNT :(
                                ExecutorService executorService = Executors.newSingleThreadExecutor();
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "/excluir_usuario.php", "POST", "UTF-8");
                                        httpRequest.setBasicAuth(Config.getLogin(AccountConfigActivity.this), Config.getPassword(AccountConfigActivity.this));
                                        httpRequest.addParam("username", Config.getUsername(AccountConfigActivity.this));

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
                                                        Config.setLogin(AccountConfigActivity.this, "");
                                                        Config.setPassword(AccountConfigActivity.this, "");
                                                        Config.setUsername(AccountConfigActivity.this, "");
                                                        Config.setName(AccountConfigActivity.this, "");

                                                        Toast.makeText(AccountConfigActivity.this, "O usuário foi excluído.", Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent(AccountConfigActivity.this, LoginActivity.class);
                                                        startActivity(i);
                                                    }
                                                });
                                            }
                                            else {
                                                final String message = jsonObject.getString("message");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(AccountConfigActivity.this, message, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        } catch (IOException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.ad_nao, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // DO NOT DELETE MY ACCOUNT!!!
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                //Intent i = new Intent(AccountConfigActivity.this, MainActivity.class);
                //startActivity(i);
            }
        });
    }
}