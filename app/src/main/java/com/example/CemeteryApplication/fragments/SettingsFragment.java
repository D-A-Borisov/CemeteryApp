package com.example.CemeteryApplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.CemeteryApplication.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Switch offlineSwitch = view.findViewById(R.id.offline_mode_switch);
        Spinner qualitySpinner = view.findViewById(R.id.quality_spinner);
        Button aboutButton = view.findViewById(R.id.about_button);
        TextView versionText = view.findViewById(R.id.version_text);

        if (getContext() != null) {
            // Офлайн режим
            offlineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    Toast.makeText(getContext(), "Загрузка карт для офлайн использования", Toast.LENGTH_LONG).show();
                }
            });

            // Качество карты
            ArrayAdapter<CharSequence> qualityAdapter = ArrayAdapter.createFromResource(
                    getContext(),
                    R.array.map_quality_options,
                    android.R.layout.simple_spinner_item
            );
            qualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qualitySpinner.setAdapter(qualityAdapter);

            versionText.setText("Версия 1.0.0");

            aboutButton.setOnClickListener(v -> {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("О приложении")
                        .setMessage("Кладбища Екатеринбурга (Социальный проект навигации по захоронениям)")
                        .setPositiveButton("OK", null)
                        .show();
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Простая проверка работы
        Switch offlineSwitch = view.findViewById(R.id.offline_mode_switch);
        if (offlineSwitch != null) {
            offlineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(getContext(), "Настройки работают! Переключатель: " + isChecked, Toast.LENGTH_SHORT).show();
            });
        }

        return view;
    }

}