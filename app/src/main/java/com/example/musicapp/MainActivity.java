package com.example.musicapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ProfileFragment profileFragment = new ProfileFragment();
    FavoritesFragment favoritesFragment = new FavoritesFragment();
    SearchFragment searchFragment = new SearchFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();

        //Here we define what each button press result of the menu will do
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                    case R.id.favorites:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,favoritesFragment).commit();
                        return true;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,searchFragment).commit();
                        return true;
                }
                return false;
            }
        });

        //static permission , this is why the window pop in the start of the program
        permission(Manifest.permission.READ_SMS);
        permission(Manifest.permission.RECEIVE_SMS);
    }

    private void permission(String permission) {
        if (ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED) {
            return;
        } else if (shouldShowRequestPermissionRationale(permission)) {
            Toast.makeText(this, "You need to grant permission to sms.", Toast.LENGTH_LONG).show();
        }
        //ask for permission from the user , launch the dialog
        requestPermissionLauncher.launch(permission);
    }

    //Here we start the service, notifying the users that they have been away for a while
    @Override
    protected void onStop () {
        startService(new Intent(this, NotificationService.class));
        super.onStop();
    }
    //Here we stop the service, notifying the users that they have been away for a while
    @Override
    protected void onResume() {
        stopService(new Intent(this, NotificationService.class));
        super.onResume();
    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
            });
}