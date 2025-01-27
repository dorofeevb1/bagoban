package su.panfilov.bogoban.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import su.panfilov.bogoban.HelpFragment;
import su.panfilov.bogoban.R;
import su.panfilov.bogoban.models.HelpItem;


public class HelpAdapter extends BaseAdapter {

    private Fragment fragment;
    public ArrayList<HelpItem> data;


    public HelpAdapter(HelpFragment f, ArrayList<HelpItem> d) {

        fragment = f;
        data = d;

    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        HelpItemHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = fragment.getLayoutInflater();
            convertView = inflater.inflate(R.layout.help_list_item, parent, false);

            holder = new HelpItemHolder();
            holder.title = convertView.findViewById(R.id.helpItemTitle);
            holder.text = convertView.findViewById(R.id.helpItemText);

            convertView.setTag(holder);
        }
        else
        {
            holder = (HelpItemHolder)convertView.getTag();
        }

        HelpItem helpItem = data.get(position);

        holder.title.setText(helpItem.getTitle());
        holder.text.setText(HtmlCompat.fromHtml(helpItem.getText(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        return convertView;
    }

    static class HelpItemHolder
    {
        public TextView title;
        public TextView text;
    }
}