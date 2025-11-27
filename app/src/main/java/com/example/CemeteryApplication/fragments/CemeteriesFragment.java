package com.example.CemeteryApplication.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.CemeteryApplication.R;
import com.example.CemeteryApplication.models.Cemetery;
import java.util.ArrayList;
import java.util.List;

public class CemeteriesFragment extends Fragment {

    public CemeteriesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cemeteries, container, false);

        Spinner cemeterySpinner = view.findViewById(R.id.cemetery_spinner);
        AutoCompleteTextView searchField = view.findViewById(R.id.search_field);
        Button searchButton = view.findViewById(R.id.search_button);
        Button mapButton = view.findViewById(R.id.map_button);
        TextView resultsTitle = view.findViewById(R.id.results_title);
        androidx.cardview.widget.CardView resultsCard = view.findViewById(R.id.results_card);

        if (getContext() != null) {
            // Настройка спиннера
            List<Cemetery> cemeteries = new ArrayList<>();
            cemeteries.add(new Cemetery("Все кладбища", ""));
            cemeteries.add(new Cemetery("Широкореченское кладбище", "ул. Репина, 1"));
            cemeteries.add(new Cemetery("Нижне-Исетское кладбище", "ул. Димитрова, 80"));
            cemeteries.add(new Cemetery("Восточное кладбище", "ул. Шефская, 2Б, корп. 8"));
            cemeteries.add(new Cemetery("Южное кладбище", "Южное клабище"));

            ArrayAdapter<Cemetery> spinnerAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    cemeteries
            );
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cemeterySpinner.setAdapter(spinnerAdapter);

            setupRussianInput(searchField);

            // Настройка автодополнения
            String[] suggestions = {"Иванов", "Петров", "Сидоров", "Квартал 1", "Квартал 2", "Аллея Славы"};
            ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    suggestions
            );
            searchField.setAdapter(searchAdapter);

            // Обработчик поиска
            searchButton.setOnClickListener(v -> {
                String query = searchField.getText().toString();
                Cemetery selected = (Cemetery) cemeterySpinner.getSelectedItem();

                if (resultsTitle != null && resultsCard != null) {
                    resultsTitle.setText("Результаты поиска: '" + query + "'");
                    resultsTitle.setVisibility(View.VISIBLE);
                    resultsCard.setVisibility(View.VISIBLE);
                }

                // Здесь будет логика поиска и отображения результатов
                Toast.makeText(getContext(), "Поиск: " + query + " на " + selected.getName(), Toast.LENGTH_SHORT).show();
            });

            // Обработчик кнопки карты - ИСПРАВЛЕНО
            mapButton.setOnClickListener(v -> {
                Cemetery selected = (Cemetery) cemeterySpinner.getSelectedItem();
                if (selected != null && !selected.getName().equals("Все кладбища")) {
                    // Используем YandexMapFragment с Яндекс Картами
                    YandexMapFragment mapFragment = YandexMapFragment.newInstance(selected.getName());

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, mapFragment)
                            .addToBackStack("cemeteries_to_yandex_map")
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Выберите конкретное кладбище", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void setupRussianInput(AutoCompleteTextView searchField) {
        // Разрешаем ввод русских символов
        searchField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        // Русская подсказка
        searchField.setHint("Введите фамилию...");

        // Минимальное количество символов для подсказок
        searchField.setThreshold(1);
    }
}