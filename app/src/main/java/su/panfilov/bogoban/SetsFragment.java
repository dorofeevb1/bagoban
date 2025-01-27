package su.panfilov.bogoban;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Map;

import su.panfilov.bogoban.components.SetsAdapter;
import su.panfilov.bogoban.models.SetItem;
import su.panfilov.bogoban.models.SetItemInfo;


public class SetsFragment extends Fragment {

    private View rootView;

    public static SetsFragment newInstance() {
        SetsFragment fragment = new SetsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sets, container, false);

        initListView(rootView);

        return rootView;
    }

    private void initListView(View rootView) {

        Map<String, SetItemInfo> setsTitlesMap = SetItem.readSetsTitlesFromCache(rootView.getContext().getApplicationContext());

        ArrayList<SetItemInfo> setItems = new ArrayList<SetItemInfo>(0);
        for (Map.Entry<String, SetItemInfo> entry : setsTitlesMap.entrySet()) {
            SetItemInfo setItemInfo = new SetItemInfo();
            setItemInfo.id = entry.getKey();
            setItemInfo.title = entry.getValue().title;
            setItems.add(setItemInfo);
        }

        SetsAdapter setsAdapter = new SetsAdapter(this, setItems);

        ListView setListView = rootView.findViewById(R.id.setsListView);
        setListView.setAdapter(setsAdapter);

    }

    public void openSaved(SetItem setItem) {
        MainActivity activity = (MainActivity) getActivity();
        SetItem setItemToOpen = new SetItem(new SetItemInfo(setItem.info.title, setItem.info.background), setItem.stoneList);
        activity.showBogoban(setItemToOpen);
    }

}
