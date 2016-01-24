package id.color.colorstable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ColorNameAdapter extends BaseAdapter {

    private ArrayList<String> list_name = new ArrayList<>();

    private Context context;
    private TextView colorName;

    public ColorNameAdapter(Context _context, ArrayList<String> _list_name) {
        this.context = _context;
        this.list_name = _list_name;

    }

    @Override
    public int getCount() {
        return list_name.size();
    }

    @Override
    public Object getItem(int position) {
        return list_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.name_listview, null);

        colorName = (TextView)convertView.findViewById(R.id.colorName);
        colorName.setText(list_name.get(position));
        return convertView;
    }
}
