package com.max.firebaseapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.max.firebaseapp.model.Upload;
import com.max.firebaseapp.util.LoadingDialog;

import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button btnUpload, btnGaleria;
    private ImageView imageView;
    private Uri imageUri = null;
    private EditText editNome;

    // referencia para um nó no RealtimeDB
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("uploads");
    private Upload upload;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        btnUpload = findViewById(R.id.update_btn_upload);
        imageView = findViewById(R.id.update_image_cel);

        btnGaleria = findViewById(R.id.update_btn_galeria);

        editNome = findViewById(R.id.update_edit_nome);

        //recuperar o upload selecionado
        upload = (Upload) getIntent().getSerializableExtra("upload");
        editNome.setText(upload.getNomeImagem());
        Glide.with(this).load(upload.getUrl()).into(imageView);

        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent();

            //intent implicita ->
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            //inicia uma Activity e espera o retorno(foto)
            startActivityForResult(intent, 112);

        });

        btnUpload.setOnClickListener(v -> {
            if (editNome.getText().toString().isEmpty()) {
                Toast.makeText(this, "Sem nome", Toast.LENGTH_SHORT).show();
                return;
            }
            //caso imagem não tenha sido atualizada.
            if(imageUri==null){
                //atualizar o nome da imagem
                String nome = editNome.getText().toString();
                upload.setNomeImagem(nome);
                database.child(upload.getId()).setValue(upload).addOnSuccessListener(aVoid -> {
                    finish();
                });
                return ;
            }
            atualizarImagem();
        });
    }
    public void atualizarImagem(){

        //deletar a imagem antiga no Storage
           storage.getReferenceFromUrl(upload.getUrl()).delete();

       //fazer upload da imagem atualizada no Storage
           uploadImagemUri();

        //atualizar no Database
    }

    private void uploadImagemUri(){

        LoadingDialog dialog = new LoadingDialog(this,R.layout.custom_dialog);

        dialog.startLoadingDialog();

        String tipo = getFileExtension(imageUri);

        //referencia do arquivo do firebase
        Date d = new Date();
        String nome = editNome.getText().toString();
        StorageReference imagemRef = storage.getReference().child("imagens/"+nome+"-"+d.getTime()+"."+tipo);
        imagemRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(this,"Upload feito com sucesso",Toast.LENGTH_SHORT).show();

                    //inserir dados da imagem no RealTimeDatabase

                    //pega a URL da imagem

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        //atualizar no database

                        //atualizar objeto upload
                        upload.setUrl(uri.toString());
                        upload.setNomeImagem(editNome.getText().toString());

                        database.child(upload.getId()).setValue(upload).addOnSuccessListener(aVoid -> {
                            dialog.dismissDialog();
                            finish();
                        });

                    });
                })
                .addOnFailureListener(e ->{
                    e.printStackTrace();
                });
    }

    private String getFileExtension(Uri imageUri){
        ContentResolver cr = getContentResolver();
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(cr.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("RESULT", "requestCode: " + requestCode + ",resultCode: " + resultCode);

        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            //caso o usuario selecionou uma imagem da galeria

            //endereco da imagem selecionada

            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

}

