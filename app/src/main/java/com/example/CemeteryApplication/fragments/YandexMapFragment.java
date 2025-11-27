package com.example.CemeteryApplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.CemeteryApplication.R;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;

import java.util.HashMap;
import java.util.Map;

public class YandexMapFragment extends Fragment {

    private static final String ARG_CEMETERY_NAME = "cemetery_name";
    private MapView mapView;
    private String cemeteryName;
    private Map<MapObject, CemeteryLocation> mapObjectMap;

    // Координаты кладбищ Екатеринбурга для Яндекс Карт
    private static final Map<String, CemeteryLocation> CEMETERY_LOCATIONS = new HashMap<String, CemeteryLocation>() {{
        put("Широкореченское кладбище", new CemeteryLocation(56.8130, 60.5272, "ул. Репина, 1"));
        put("Нижне-Исетское кладбище", new CemeteryLocation(56.7360, 60.6981, "ул. Димитрова, 80"));
        put("Восточное кладбище", new CemeteryLocation(56.8799, 60.6605, "ул. Шефская, 2Б, корп. 8"));
        put("Южное кладбище", new CemeteryLocation(56.6973, 60.7337, "Южное клабище"));
    }};

    // Центр Екатеринбурга
    private static final Point EKATERINBURG_CENTER = new Point(56.8380, 60.5970);

    // Tap listener для меток
    private final MapObjectTapListener mapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            CemeteryLocation location = mapObjectMap.get(mapObject);
            if (location != null) {
                showCemeteryInfo(location);
                return true;
            }
            return false;
        }
    };

    public YandexMapFragment() {
        // Required empty public constructor
    }

    public static YandexMapFragment newInstance(String cemeteryName) {
        YandexMapFragment fragment = new YandexMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CEMETERY_NAME, cemeteryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cemeteryName = getArguments().getString(ARG_CEMETERY_NAME);
        }
        mapObjectMap = new HashMap<>();

        // Инициализация MapKit (должна быть выполнена до создания MapView)
        MapKitFactory.initialize(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yandex_map, container, false);

        // Находим MapView по правильному ID
        mapView = view.findViewById(R.id.map_view);
        setupUI(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMap();
    }

    private void setupMap() {
        if (mapView == null || mapView.getMap() == null) return;

        // Настройка начальной позиции камеры
        if (cemeteryName != null && !cemeteryName.equals("Все кладбища")) {
            // Перемещение к выбранному кладбищу
            CemeteryLocation location = CEMETERY_LOCATIONS.get(cemeteryName);
            if (location != null) {
                Point point = new Point(location.latitude, location.longitude);
                mapView.getMap().move(
                        new CameraPosition(point, 15.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1.0f),
                        null
                );
                addCemeteryMarker(location, cemeteryName);
            }
        } else {
            // Показать все кладбища
            mapView.getMap().move(
                    new CameraPosition(EKATERINBURG_CENTER, 12.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 1.0f),
                    null
            );
            addAllCemeteryMarkers();
        }
    }

    private void addAllCemeteryMarkers() {
        for (Map.Entry<String, CemeteryLocation> entry : CEMETERY_LOCATIONS.entrySet()) {
            addCemeteryMarker(entry.getValue(), entry.getKey());
        }
    }

    private void addCemeteryMarker(CemeteryLocation location, String name) {
        Point point = new Point(location.latitude, location.longitude);

        // Создаем метку
        PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(point);

        // Настраиваем внешний вид метки
        placemark.setOpacity(0.9f);
        placemark.setUserData(name);

        // Добавляем обработчик клика
        placemark.addTapListener(mapObjectTapListener);

        mapObjectMap.put(placemark, location);
    }

    private void showCemeteryInfo(CemeteryLocation location) {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Информация о кладбище")
                .setMessage("Адрес: " + location.address +
                        "\nШирота: " + String.format("%.6f", location.latitude) +
                        "\nДолгота: " + String.format("%.6f", location.longitude))
                .setPositiveButton("OK", null)
                .setNeutralButton("Построить маршрут", (dialog, which) -> {
                    openYandexNavigator(location);
                })
                .show();
    }

    private void openYandexNavigator(CemeteryLocation location) {
        // Открываем Яндекс Навигатор с маршрутом
        try {
            String uri = "yandexnavi://build_route_on_map?" +
                    "lat_to=" + location.latitude +
                    "&lon_to=" + location.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri));
            startActivity(intent);
        } catch (Exception e) {
            // Если Яндекс Навигатор не установлен
            Toast.makeText(requireContext(), "Установите Яндекс Навигатор", Toast.LENGTH_SHORT).show();

            // Альтернатива - открыть в браузере
            String url = "https://yandex.ru/maps/?pt=" + location.longitude +
                    "," + location.latitude + "&z=15&l=map";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    private void setupUI(View view) {
        // Кнопка назад
        Button backButton = view.findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        // Заголовок
        TextView mapTitle = view.findViewById(R.id.map_title);
        if (mapTitle != null) {
            String title = cemeteryName != null ? "Карта: " + cemeteryName : "Карта кладбищ Екатеринбурга";
            mapTitle.setText(title);
        }


        // Кнопка показа всех кладбищ
        Button showAllButton = view.findViewById(R.id.show_all_button);
        if (showAllButton != null) {
            showAllButton.setOnClickListener(v -> {
                if (mapView != null && mapView.getMap() != null) {
                    mapView.getMap().getMapObjects().clear();
                    mapObjectMap.clear();
                    addAllCemeteryMarkers();
                    mapView.getMap().move(
                            new CameraPosition(EKATERINBURG_CENTER, 12.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 1.0f),
                            null
                    );
                }
            });
        }

        // Кнопки zoom
        Button zoomInButton = view.findViewById(R.id.zoom_in_button);
        if (zoomInButton != null) {
            zoomInButton.setOnClickListener(v -> {
                if (mapView != null && mapView.getMap() != null) {
                    CameraPosition currentPosition = mapView.getMap().getCameraPosition();
                    mapView.getMap().move(
                            new CameraPosition(
                                    currentPosition.getTarget(),
                                    currentPosition.getZoom() + 1,
                                    currentPosition.getAzimuth(),
                                    currentPosition.getTilt()
                            ),
                            new Animation(Animation.Type.SMOOTH, 0.3f),
                            null
                    );
                }
            });
        }

        Button zoomOutButton = view.findViewById(R.id.zoom_out_button);
        if (zoomOutButton != null) {
            zoomOutButton.setOnClickListener(v -> {
                if (mapView != null && mapView.getMap() != null) {
                    CameraPosition currentPosition = mapView.getMap().getCameraPosition();
                    mapView.getMap().move(
                            new CameraPosition(
                                    currentPosition.getTarget(),
                                    currentPosition.getZoom() - 1,
                                    currentPosition.getAzimuth(),
                                    currentPosition.getTilt()
                            ),
                            new Animation(Animation.Type.SMOOTH, 0.3f),
                            null
                    );
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) {
            MapKitFactory.getInstance().onStart();
            mapView.onStart();
        }
    }

    @Override
    public void onStop() {
        if (mapView != null) {
            mapView.onStop();
            MapKitFactory.getInstance().onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (mapView != null) {
            mapView.getMap().getMapObjects().clear();
            mapObjectMap.clear();
        }
        super.onDestroyView();
    }

    // Вспомогательный класс для хранения данных о кладбищах
    private static class CemeteryLocation {
        double latitude;
        double longitude;
        String address;

        CemeteryLocation(double latitude, double longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }
    }
}