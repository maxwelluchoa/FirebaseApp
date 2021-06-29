package com.max.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        btnMenu = findViewById(R.id.navigation_icon);
        drawerLayout = findViewById(R.id.nav_drawerLayout);

        btnMenu.setOnClickListener(view ->{
            drawerLayout.openDrawer(GravityCompat.START);
        });
        //Navigation View Menu
        NavigationView navigationView = findViewById(R.id.nav_navigationView);

        //NavController
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        // Configura um NavigationView para usar como NavController
        NavigationUI.setupWithNavController(navigationView,navController);
    }
}
