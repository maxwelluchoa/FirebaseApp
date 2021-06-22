package com.max.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.max.firebaseapp.model.Upload;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button btnLogout,btnStorage;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("uploads");
    private ArrayList<Upload> listaUploads = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         btnLogout = findViewById(R.id.main_btn_logout);
         btnStorage = findViewById(R.id.main_btn_storage);

         btnStorage.setOnClickListener(v ->{
             //abri StorageActivity

                 Intent intent = new Intent(getApplicationContext(), StorageActivity.class);
                 startActivity(intent);
         });

        btnLogout.setOnClickListener(v -> {

             //deslogar usuário
             auth.signOut();
             finish();
         });

         TextView textEmail = findViewById(R.id.main_text_email);
        textEmail.setText(auth.getCurrentUser().getEmail());

        TextView textNome = findViewById(R.id.main_text_user);
        textNome.setText(auth.getCurrentUser().getDisplayName());


    }

    @Override
    protected void onStart() {
        //onStart:
        //- Faz parte do clico de vida da Activity,depois do onCreate()
        //- É executado quando o app inicia,
        // - e quando volta do background
        super.onStart();
        getData();
    }
    public void getData(){

        //listener para o nó uploads
        //- caso ocorra alguma alteração -> retorna Todos os dados
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot no_filho : snapshot.getChildren()){
                    Upload upload = no_filho.getValue(Upload.class);
                    listaUploads.add(upload);
                    Log.i("DATABASE","id" + upload.getId()+ ",nome" +upload.getNomeImagem());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
