package com.max.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.max.firebaseapp.model.User;

public class CadastroActivity extends AppCompatActivity {
    private Button btnCadastrar;
    private EditText editEmail, editNome, editSenha;

    //referência para autenticação
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        btnCadastrar = findViewById(R.id.cadastro_btn_cadastrar);
        editEmail = findViewById(R.id.cadastro_edit_email);
        editNome = findViewById(R.id.cadastro_edit_nome);
        editSenha = findViewById(R.id.cadastro_edit_senha);

        btnCadastrar.setOnClickListener(view -> {
        cadastrar();
        });

    }
    public void cadastrar(){
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        String nome = editNome.getText().toString();

        if(email.isEmpty() || senha.isEmpty() || nome.isEmpty()){
            Toast.makeText(this,"Preencha os campos",Toast.LENGTH_SHORT).show();
            return;
        }
        //criar um usuario com email e senha
       Task <AuthResult> t = auth.createUserWithEmailAndPassword(email,senha);
        t.addOnCompleteListener(task -> {

            //listener executado com sucesso ou fracasso
            if(task.isSuccessful()){
                Toast.makeText(getApplicationContext(),"Usuário criado com sucesso ! ",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Erro",Toast.LENGTH_SHORT).show();
            }
        });

        t.addOnSuccessListener(authResult -> {

            //request para mudar o nome do usuário
            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();

            //inserir no database
            User u = new User(authResult.getUser().getUid(),email,nome);
            database.child(u.getId()).setValue(u);


            //setando nome do usuário
            authResult.getUser().updateProfile(update);
        });
    }
}


