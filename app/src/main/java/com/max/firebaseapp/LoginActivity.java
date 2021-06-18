package com.max.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private Button btnLogin;
    private EditText editEmail,editSenha;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnCadastrar = findViewById(R.id.login_btn_cadastrar);
        btnLogin = findViewById(R.id.login_btn_cadastrar);
        editEmail = findViewById(R.id.login_edit_email);
        editSenha = findViewById(R.id.login_edit_senha);

        //caso usuário logado

        if (auth.getCurrentUser()!=null){
            String email = auth.getCurrentUser().getEmail();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);

            //passar o email p/ MainActivity
            intent.putExtra("email",email);
            startActivity(intent);
        }

        btnCadastrar.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(view -> {
            logar();
        });
    }
            public void logar() {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // t - > é uma tarefa para logar
                auth.signInWithEmailAndPassword(email, senha)

                //listener com sucesso
                .addOnSuccessListener(authResult -> {
                   Toast.makeText(this,"Bem Vindo",Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                   startActivity(intent);
                })
                .addOnFailureListener(e -> {

              /*      //parametro e - > Excepction
                    Toast.makeText(this,"Erro"+
                    e.getClass().toString(),Toast.LENGTH_SHORT).show();
                    Log.e("Erro","Mensagem: " +e.getMessage()+"classe:"+e.getClass().toString());
             */
                    try {
                        throw e;
                    }catch(FirebaseAuthInvalidUserException userException){

                        //Exceção para email inválido
                        Toast.makeText(this, "Email Inválido!", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthInvalidCredentialsException credException){

                        //Exceção para senha incorreta
                        Toast.makeText(this, "Senha Incorreta!", Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){

                        //Exceção Genérica
                       Toast.makeText(this,"Erro",Toast.LENGTH_SHORT).show();
                    }
                });
            }

    private void addOnSuccessListener(Object bem_vindo) {
    }
}

