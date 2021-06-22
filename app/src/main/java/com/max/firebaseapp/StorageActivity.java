package com.max.firebaseapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.max.firebaseapp.model.Upload;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class StorageActivity extends AppCompatActivity {

    //referencia p/ firebasestorage
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button btnUpload,btnGaleria;
    private ImageView imageView;
    private Uri imageUri=null;
    private EditText editNome;

    // referencia para um nó no RealtimeDB
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("uploads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        btnUpload = findViewById(R.id.storage_btn_upload);
        imageView = findViewById(R.id.storage_image_cel);

        btnGaleria = findViewById(R.id.storage_btn_galeria);

        editNome = findViewById(R.id.storage_edit_nome);

        btnUpload.setOnClickListener(v -> {
            if(editNome.getText().toString().isEmpty()){
                Toast.makeText(this,"Digite um nome para Imagem",Toast.LENGTH_SHORT).show();
                return;
            }
            if (imageUri != null) {
                uploadImagemUri();
            }else{
                uploadImagemByte();
            }
        });

        btnGaleria.setOnClickListener(v ->{
            Intent intent = new Intent();

            //intent implicita ->
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            //inicia uma Activity e espera o retorno(foto)
            startActivityForResult(intent,112);

        });
    }
    private void uploadImagemUri(){
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
                    //inserir no database

                     //criando referencia (database do upload)
                     DatabaseReference refUpload = database.push();
                     String id = refUpload.getKey();

                     Upload upload = new Upload(id,nome,uri.toString());
                     //salvando upload no DB

                     refUpload.setValue(upload);
                 });
                })
                .addOnFailureListener(e ->{
                    e.printStackTrace();
                });
    }
    //retornar o tipo (.png,.jpg) da imagem
    private String getFileExtension(Uri imageUri){
        ContentResolver cr = getContentResolver();
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(cr.getType(imageUri));
    }

     //resultado do starActivityResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("RESULT","requestCode: "+requestCode+",resultCode: "+resultCode);

        if(requestCode==112 && resultCode== Activity.RESULT_OK){
            //caso o usuario selecionou uma imagem da galeria

            //endereco da imagem selecionada

            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    public byte[] convertImage2Byte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //byte[] data = baos.toByteArray();
        return baos.toByteArray();
    }


    //fazer upload de uma imagem convertida p/ bytes
    public void uploadImagemByte() {
        byte[] data = convertImage2Byte(imageView);

        //criar uma referencia para uma imagem no Storage
        StorageReference imagemRef = storage.getReference().child("imagens/01.jpeg");

        //realiza o upload da imagem
        imagemRef.putBytes(data)
        .addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this,"Upload feito com sucesso!",Toast.LENGTH_SHORT).show();
            Log.i("UPLOAD","Sucesso");
        })

            .addOnFailureListener(e ->{
            e.printStackTrace();
        });

    }
}

