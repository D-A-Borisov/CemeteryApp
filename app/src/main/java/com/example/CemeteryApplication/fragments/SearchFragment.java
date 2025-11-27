package com.example.CemeteryApplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.CemeteryApplication.R;
import com.example.CemeteryApplication.models.Cemetery;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Spinner cemeterySpinner = view.findViewById(R.id.cemetery_spinner);
        AutoCompleteTextView searchField = view.findViewById(R.id.search_field);
        Button searchButton = view.findViewById(R.id.search_button);

        if (cemeterySpinner != null && searchField != null && searchButton != null && getContext() != null) {
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

            // Настройка автодополнения
            String[] suggestions = {"Иванов", "Петров", "Сидоров", "Квартал 1", "Квартал 2"};
            ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    suggestions
            );
            searchField.setAdapter(searchAdapter);

            searchButton.setOnClickListener(v -> {
                String query = searchField.getText().toString();
                Cemetery selected = (Cemetery) cemeterySpinner.getSelectedItem();
                Toast.makeText(getContext(), "Поиск: " + query + " на " + selected.getName(), Toast.LENGTH_SHORT).show();
            });
        }

        return view;
    }
}
