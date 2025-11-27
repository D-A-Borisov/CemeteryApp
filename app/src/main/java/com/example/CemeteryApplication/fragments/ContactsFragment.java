package com.example.CemeteryApplication.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.CemeteryApplication.R;

public class ContactsFragment extends Fragment {

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        // Обработчики для контактов
        View emailLayout = view.findViewById(R.id.email_layout);
        View phoneLayout = view.findViewById(R.id.phone_layout);

        if (emailLayout != null) {
            emailLayout.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:info@cemeteries-ekb.ru"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Вопрос по приложению Кладбища Екатеринбурга");
                    startActivity(Intent.createChooser(intent, "Отправить email"));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Email приложение не найдено", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (phoneLayout != null) {
            phoneLayout.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+73430000000"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Невозможно совершить звонок", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}