package su.panfilov.bogoban;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class OurAppsFragment extends Fragment {

    private ImageView neurocafeBanner;
    private ImageView metamodernBanner;
    private ImageView pyramidBanner;

    public static OurAppsFragment newInstance() {
        return new OurAppsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_our_apps, container, false);

        // Кнопка "Назад"
        Button backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        // Инициализация баннеров
        neurocafeBanner = rootView.findViewById(R.id.neurocafeBanner);
        metamodernBanner = rootView.findViewById(R.id.metamodernBanner);
        pyramidBanner = rootView.findViewById(R.id.pyramidBanner);

        // НейроКафе
        neurocafeBanner.setOnClickListener(v -> openAppOrStore(
                "su.panfilov.neurocafe", // Пакет для Android
                "https://play.google.com/store/search?q=%D0%9D%D0%B5%D0%B9%D1%80%D0%BE%D0%BA%D0%B0%D1%84%D0%B5&c=apps", // Google Play
                "https://apps.apple.com/ru/app/%D0%BD%D0%B5%D0%B9%D1%80%D0%BE%D0%BA%D0%B0%D1%84%D0%B5/id1612475980" // App Store
        ));

        // MetaModern
        metamodernBanner.setOnClickListener(v -> openAppOrStore(
                "su.panfilov.metamodern",
                "https://play.google.com/store/apps/details?id=su.panfilov.metamodern",
                "https://apps.apple.com/ru/app/metamodern/id1484083509"
        ));

        // Пирамида
        pyramidBanner.setOnClickListener(v -> openAppOrStore(
                "su.panfilov.piramida",
                "https://play.google.com/store/apps/details?id=su.panfilov.piramida",
                "https://apps.apple.com/us/app/ipyramid/id1378007915"
        ));

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Очистка ресурсов (изображений)
        if (neurocafeBanner != null) {
            Glide.with(this).clear(neurocafeBanner);
            neurocafeBanner = null;
        }
        if (metamodernBanner != null) {
            Glide.with(this).clear(metamodernBanner);
            metamodernBanner = null;
        }
        if (pyramidBanner != null) {
            Glide.with(this).clear(pyramidBanner);
            pyramidBanner = null;
        }
    }

    /**
     * Открывает приложение, если оно установлено, или переходит в магазин.
     *
     * @param packageName Пакет приложения для Android.
     * @param playStoreUrl Ссылка на Google Play.
     * @param appStoreUrl Ссылка на App Store.
     */
    private void openAppOrStore(String packageName, String playStoreUrl, String appStoreUrl) {
        try {
            // Проверяем, установлено ли приложение
            PackageManager pm = requireContext().getPackageManager();
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

            // Если приложение установлено, открываем его
            Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Приложение не установлено, открываем магазин
            Intent storeIntent = new Intent(Intent.ACTION_VIEW);
            storeIntent.setData(Uri.parse(playStoreUrl)); // По умолчанию для Android
            startActivity(storeIntent);
        }
    }
}