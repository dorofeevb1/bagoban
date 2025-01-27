package su.panfilov.bogoban;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import su.panfilov.bogoban.components.HelpAdapter;
import su.panfilov.bogoban.models.HelpItem;

public class HelpFragment extends Fragment {

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        initListView(rootView);

        // Обработка кнопки "Ссылки"
        Button linksButton = rootView.findViewById(R.id.linksButton);
        linksButton.setOnClickListener(v -> showLinksDialog());

        // Обработка кнопки "Наши приложения"
        Button ourAppsButton = rootView.findViewById(R.id.ourAppsButton);
        ourAppsButton.setOnClickListener(v -> {
            OurAppsFragment ourAppsFragment = OurAppsFragment.newInstance();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootLayout, ourAppsFragment) // Используем rootLayout
                    .addToBackStack(null)
                    .commit();
        });

        return rootView;
    }

    private void initListView(View rootView) {
        String[] titles = getResources().getStringArray(R.array.help_titles);
        String[] contents = getResources().getStringArray(R.array.help_contents);

        ArrayList<HelpItem> helpItems = new ArrayList<>(0);

        for (int i = 0; i < titles.length; i++) {
            if (!(contents.length > i)) {
                continue;
            }

            HelpItem helpItem = new HelpItem(titles[i], contents[i]);
            helpItems.add(helpItem);
        }

        HelpAdapter helpAdapter = new HelpAdapter(this, helpItems);
        ListView helpListView = rootView.findViewById(R.id.helpListView);
        helpListView.setAdapter(helpAdapter);
    }

    private void showLinksDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_links);

        TextView link1 = dialog.findViewById(R.id.link1);
        TextView link2 = dialog.findViewById(R.id.link2);
        TextView link3 = dialog.findViewById(R.id.link3);
        TextView link4 = dialog.findViewById(R.id.link4);
        Button closeButton = dialog.findViewById(R.id.closeButton);

        link1.setOnClickListener(v -> openUrl("https://piskarev.ru"));
        link2.setOnClickListener(v -> openUrl("https://neurographica.metamodern.ru"));
        link3.setOnClickListener(v -> openUrl("https://calendar.metamodern.ru"));
        link4.setOnClickListener(v -> openUrl("https://education.neurograff.com/chtm/screens~navigate"));

        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}