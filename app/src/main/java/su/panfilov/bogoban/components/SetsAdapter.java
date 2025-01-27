package su.panfilov.bogoban.components;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import su.panfilov.bogoban.SetsFragment;
import su.panfilov.bogoban.R;
import su.panfilov.bogoban.models.SetItem;
import su.panfilov.bogoban.models.SetItemInfo;


public class SetsAdapter extends BaseAdapter {

    private static final String TAG = "DiariesAdapter";

    private SetsFragment fragment;
    public ArrayList<SetItemInfo> data;


    public SetsAdapter(SetsFragment f, ArrayList<SetItemInfo> d) {

        fragment = f;
        data = d;

    }

    public int getCount() {
        return data.size() == 0 ? 1 : data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (data.size() == 0) {
            LayoutInflater inflater = fragment.getLayoutInflater();
            convertView = inflater.inflate(R.layout.sets_list_item_empty, parent, false);
            return convertView;
        }

        SetItemHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = fragment.getLayoutInflater();
            convertView = inflater.inflate(R.layout.sets_list_item, parent, false);

            holder = new SetItemHolder();
            holder.title = convertView.findViewById(R.id.setItemTitle);
            holder.editButton = convertView.findViewById(R.id.editSetButton);
            holder.deleteButton = convertView.findViewById(R.id.removeSetButton);

            convertView.setTag(holder);
        }
        else
        {
            holder = (SetItemHolder)convertView.getTag();
        }

        String setId = data.get(position).id;
        final SetItem setItem = SetItem.readSetFromCache(fragment.getContext().getApplicationContext(), setId);

        Log.d(TAG, "getView: " + holder.title);

        holder.title.setText(setItem.info.title);
        holder.title.setOnClickListener(view -> fragment.openSaved(setItem));
        holder.editButton.setOnClickListener(view -> {

        });
        holder.deleteButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
            builder.setTitle("Удалить запись?");

            builder.setPositiveButton("Да", (dialog, which) -> {
                setItem.delete(fragment.getContext().getApplicationContext());
                data.remove(position);
                notifyDataSetChanged();
            });
            builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        return convertView;
    }

    static class SetItemHolder
    {
        public TextView title;
        public ImageButton deleteButton;
        public ImageButton editButton;
    }
}