package com.max.firebaseapp.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.navigation.NavDeepLinkBuilder;

import com.max.firebaseapp.NavigationActivity;
import com.max.firebaseapp.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toast");
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent();
        intent1.setClassName("com.max.firebaseapp"
                ,"com.max.firebaseapp.NavigationActiviy");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

       /* PendingIntent intentFragment = new NavDeepLinkBuilder(context)
                                           .setComponentName(NavigationActivity.class)
                                           .setGraph(R.navigation.nav_graph)
                                           .setDestination(R.id.nav_menu_cadastro_imagem)
                                           .createPendingIntent();

        PendingIntent.
        */
       context.startActivity(intent);
    }
}
