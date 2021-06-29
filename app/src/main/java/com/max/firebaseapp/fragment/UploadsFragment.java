package com.max.firebaseapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.max.firebaseapp.R;
import com.max.firebaseapp.UpdateActivity;
import com.max.firebaseapp.adapter.ImageAdapter;
import com.max.firebaseapp.model.Upload;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadsFragment extends Fragment {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button btnLogout,btnStorage;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference("uploads");
    private ArrayList<Upload> listaUploads = new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    public UploadsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.main_btn_logout);
        btnStorage = findViewById(R.id.main_btn_storage);

        recyclerView = findViewById((R.id.main_recycler));

        imageAdapter = new ImageAdapter(getApplicationContext(),listaUploads);

        imageAdapter.setListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Upload upload = listaUploads.get(position);
                deleteUpload(upload);
            }

            @Override
            public void onUpdateClick(int position) {
                Upload upload = listaUploads.get(position);
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);

                //envia o upload pra outra Activity
                intent.putExtra("upload",upload);
                startActivity(intent);
            }
        });



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uploads, container, false);
    }
}
