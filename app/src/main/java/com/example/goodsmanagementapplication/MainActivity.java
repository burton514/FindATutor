package com.example.goodsmanagementapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    TextView tvNameHeader,tvEmailHeader;
    NavigationView navigationView;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ẩn thanh trạng thái
        getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());

        navigationView = findViewById(R.id.nav_view);

        tvNameHeader = navigationView.getHeaderView(0).findViewById(R.id.tvNameHeader);
        tvEmailHeader = navigationView.getHeaderView(0).findViewById(R.id.tvEmailHeader);
        //Thiết lập toolbar như 1 actionbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        toolbar.bringToFront();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity();
            }
        });
        
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                Fragment fragment = null;
                if(item.getItemId()==R.id.nav_home) {
                    fragment = new HomeFragment();
                    mDrawerLayout.closeDrawers();
                }
                else if(item.getItemId()==R.id.nav_settings) {
                    fragment = new SettingFragment();
                    mDrawerLayout.closeDrawers();
                }
                else if(item.getItemId()==R.id.nav_profile) {
                    fragment = new UserProfileFragment();
                    mDrawerLayout.closeDrawers();
                }
                else if(item.getItemId()==R.id.nav_logOut) {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }

                return true;
            }
        });
        initUser();
    //
    }

    private void startChatActivity() {
        Intent intent = new Intent(MainActivity.this,ChatActivity.class);
        startActivity(intent);
    }

    private void initUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        tvNameHeader.setText(user.getDisplayName().toString().trim());
        tvEmailHeader.setText(user.getEmail().toString().trim());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Mở Navigation Drawer khi click vào button menu
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}