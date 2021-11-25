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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  activity fica em "tela cheia"
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  construcao do layout
        setContentView(R.layout.activity_login);

        /* Atividade de Login
         * Realiza uma conexao com o servidor para fazer uma consulta no banco de dados e procurar o usuario
         * Se o email e a senha informadas estiverem corretos com os dados do servidor, entao o usuario loga e acessa a app
         * Caso contratio, ele nao podera usar o app ate fazer o registro ou entrar com dados validos
         */
        Button btnLogin = findViewById(R.id.btnEntrar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);

                //  obtendo dados passados para login
                EditText et_email_login = findViewById(R.id.et_email_login);
                final String username = et_email_login.getText().toString();
                if(username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username não informado", Toast.LENGTH_LONG).show();
                    view.setEnabled(true);
                    return;
                }

                EditText et_senha_login = findViewById(R.id.et_senha_login);
                final String senha = et_senha_login.getText().toString();
                if(senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Senha não informada", Toast.LENGTH_LONG).show();
                    view.setEnabled(true);
                    return;
                }

                //  nova thread para conectar com o servidor
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequest httpRequest = new HttpRequest(Config.SERVER_URL_BASE + "login.php", "POST", "UTF-8");
                        httpRequest.setBasicAuth(username, senha);

                        try {
                            InputStream is = httpRequest.execute();
                            String result = Util.inputStream2String(is, "UTF-8");
                            httpRequest.finish();

                            Log.d("HTTP_REQUEST_RESULT", result);

                            JSONObject jsonObject = new JSONObject(result);
                            final int success = jsonObject.getInt("success");
                            if(success == 1) {
                                /* Nesse caso, o usuário existe no sistema
                                 * Entao, vamos setar essas configuracoes de email e senha em um arquivo para uso futuro
                                 * e redirecionar o usuario para "dentro" da app (mainActivity)
                                 */
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Config.setLogin(LoginActivity.this, username);
                                        Config.setPassword(LoginActivity.this, senha);
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                });

                            } else {
                                // Nesse caso, o usuario nao pode usar a app e deve tentar fazer login ou se cadastrar
                                final String erro = jsonObject.getString("error");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_LONG).show();
                                        view.setEnabled(true);
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, (CharSequence) e, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    });

            }
        });

        /* Navigation between screens */

        TextView tvCadastrese = findViewById(R.id.tv_cadastrese);
        tvCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        TextView tvEsqueciSenha = findViewById(R.id.tv_esqueceu_senha);
        tvEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }
}