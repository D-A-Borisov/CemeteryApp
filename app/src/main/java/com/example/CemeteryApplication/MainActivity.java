package com.example.CemeteryApplication;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.CemeteryApplication.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.MapKitFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация Яндекс Карт ДО setContentView
        initializeYandexMaps();

        setContentView(R.layout.activity_main);
        setupNavigation(savedInstanceState);
    }

    private void initializeYandexMaps() {
        try {
            String apiKey = BuildConfig.YANDEX_MAPS_API_KEY;

            if (apiKey == null || apiKey.isEmpty()) {
                Log.e("Maps", "API ключ Яндекс Карт не найден!");
                return;
            }

            Log.d("Maps", "Инициализация Яндекс Карт");
            MapKitFactory.setApiKey(apiKey);

        } catch (Exception e) {
            Log.e("Maps", "Ошибка инициализации карт: " + e.getMessage());
        }
    }

    private void setupNavigation(Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_cemeteries) {
                selectedFragment = new CemeteriesFragment();
            } else if (itemId == R.id.nav_about) {
                selectedFragment = new AboutFragment();
            } else if (itemId == R.id.nav_contacts) {
                selectedFragment = new ContactsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Стартовый фрагмент
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            if (bottomNav != null && getSupportFragmentManager().getBackStackEntryCount() == 0) {
                bottomNav.setSelectedItemId(R.id.nav_home);
            }
        } else {
            super.onBackPressed();
        }
    }
}