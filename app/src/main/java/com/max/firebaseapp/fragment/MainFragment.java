package com.max.firebaseapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.max.firebaseapp.R;
import com.max.firebaseapp.adapter.UserAdapter;
import com.max.firebaseapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private RecyclerView recyclerContatos;
    private UserAdapter userAdapter;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

    private DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("requests");

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ArrayList<User> listaContatos = new ArrayList<>();
    private User userLogged;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        userLogged = new User(auth.getCurrentUser().getUid(),auth.getCurrentUser().getEmail(),auth.getCurrentUser().getDisplayName());

        recyclerContatos = layout.findViewById(R.id.frag_main_recycler_user);

        userAdapter = new UserAdapter(getContext(),listaContatos);

        userAdapter.setListener(new UserAdapter.ClickAdapterUser() {
            @Override
            public void adicionarContato(int position) {
                User u = listaContatos.get(position);

                //request send
                  requestRef.child(userLogged.getId()).child("send").setValue(u);

                 // request receive
                requestRef.child(u.getId()).child("receive").child(userLogged.getId()).setValue(userLogged);

                  //tirar o usu??rio solicitado
                listaContatos.remove(position).setReceiveRequest(true);
                userAdapter.notifyDataSetChanged();

            }
        });

        recyclerContatos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerContatos.setAdapter(userAdapter);


        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUsersDatabase();
    }

    public void getUsersDatabase(){

        //ir?? armazenar usu??rios que j?? foram solicitados
        Map<String,User> mapUsersReq = new HashMap<String,User>();
         requestRef.child(userLogged.getId()).child("send").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot u: snapshot.getChildren()){
                     User user = u.getValue(User.class);

                     //adicionando usu??rio no HashMap
                     mapUsersReq.put(user.getId(),user);

                 }

                 //ler o N?? no Usu??rios
                 usersRef.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         listaContatos.clear();
                         for(DataSnapshot u: snapshot.getChildren()){
                             User user = u.getValue(User.class);
                             if(mapUsersReq.containsKey(user.getId())){
                                 user.setReceiveRequest(true);
                             }
                             if(!userLogged.equals(user)){
                                 listaContatos.add(user);
                             }
                         }
                         userAdapter.notifyDataSetChanged();
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}