package com.example.CemeteryApplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.CemeteryApplication.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Основные кнопки
        Button searchButton = view.findViewById(R.id.search_button);

        // Карточки быстрого доступа
        View listCard = view.findViewById(R.id.list_card);
        View mapCard = view.findViewById(R.id.map_card);
        View settingsCard = view.findViewById(R.id.settings_card);

        // Обработчики для основных кнопок (через нижнее меню)
        searchButton.setOnClickListener(v -> {
            switchToFragment(R.id.nav_cemeteries);
        });

        // Обработчики для карточек быстрого доступа (прямые переходы)
        listCard.setOnClickListener(v -> {
            openCemeteriesFragmentDirectly();
        });

        mapCard.setOnClickListener(v -> {
            openYandexMapFragmentDirectly();
        });

        settingsCard.setOnClickListener(v -> {
            openSettingsFragmentDirectly();
        });

        return view;
    }

    private void switchToFragment(int menuItemId) {
        if (getActivity() instanceof com.example.CemeteryApplication.MainActivity) {
            com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                    getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(menuItemId);
            }
        }
    }

    private void openCemeteriesFragmentDirectly() {
        CemeteriesFragment fragment = new CemeteriesFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("home_to_cemeteries")
                .commit();
    }

    private void openYandexMapFragmentDirectly() {
        YandexMapFragment mapFragment = YandexMapFragment.newInstance("Все кладбища");

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .addToBackStack("home_to_yandex_map")
                .commit();
    }

    private void openSettingsFragmentDirectly() {
        SettingsFragment fragment = new SettingsFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("home_to_settings")
                .commit();
    }
}