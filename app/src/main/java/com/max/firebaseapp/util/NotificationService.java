package com.max.firebaseapp.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.max.firebaseapp.R;
import com.max.firebaseapp.model.User;

import static com.max.firebaseapp.util.App.CHANNEL_1;

public class NotificationService extends Service {

    private ValueEventListener listener;
    private DatabaseReference receiveRef;


    @Override
    public void onCreate() {
        super.onCreate();
    }


        //é executado quando o serviço e executado -> uma vez


    public void showNotify(User user){

        //criando notificação
        Notification notification = new NotificationCompat
                .Builder(getApplicationContext(),CHANNEL_1)
                .setSmallIcon(R.drawable.ic_account_circle_black_24dp)
                .setContentTitle("Alteração")
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        //enviando para CHANNEL
        NotificationManager nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startID){
         receiveRef = FirebaseDatabase
                .getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("receive");

        listener = receiveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                showNotify(user);
                //finaliza o service
               onRebind(new Intent());
                // stopSelf();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       return super.onStartCommand(intent,flags,startID);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        receiveRef.removeEventListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}



