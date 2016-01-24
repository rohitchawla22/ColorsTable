package id.color.colorstable;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Fragment4 extends Fragment {
    private float hue_1, hue_2, saturation, values;
    private ListView basecolor_listview_4;
    private Uri uri = TaskContract.CONTENT_URI;
    private ArrayList<String> list_name ;
    private ArrayList<String> list_hue ;
    private ArrayList<String> list_saturation ;
    private ArrayList<String> list_value;
    private ArrayList<String> list_sorted_value ;

    Cursor cursor;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        list_name = new ArrayList<>();
        list_hue = new ArrayList<>();
        list_saturation = new ArrayList<>();
        list_value = new ArrayList<>();
        list_sorted_value = new ArrayList<>();

        final View rootView = inflater.inflate(R.layout.base_color, container, false);
        Button confButtonH = (Button) rootView.findViewById(R.id.confButton);
        confButtonH.setVisibility(View.INVISIBLE);
        confButtonH.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        Button searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setVisibility(View.INVISIBLE);
        searchButton.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            hue_1 = bundle.getFloat("hue_1", 0);
            hue_2 = bundle.getFloat("hue_2", 0);
            saturation = bundle.getFloat("saturation", 0);
            values = bundle.getFloat("values", 0);
        }

        Clear_list();

        String searchQuery="";
        String  task_min = hue_1+"";
        String task_max = hue_2+"";
        String task_sat = saturation+"";
        String task_val = values+"";
        if(hue_1>hue_2){

            searchQuery =  TaskContract.Columns.HUE+" > '" + task_min
                    + "' AND "+TaskContract.Columns.HUE+" < '" + "360.0"+"'"
                    + " AND "+TaskContract.Columns.SATURATION+" = '" + task_sat+"'"
                    + " AND "+TaskContract.Columns.VALUE+" = '" + task_val+"'"
                    + " OR "+ TaskContract.Columns.HUE+" < '" + task_max
                    + "' AND "+TaskContract.Columns.HUE+" >'" + "0.0"+"'"
                    + " AND "+TaskContract.Columns.SATURATION+" = '" + task_sat+"'"
                    + " AND "+TaskContract.Columns.VALUE+" = '" + task_val+"'"
            ;
        }
        else{

            searchQuery =  TaskContract.Columns.HUE+" > '" + task_min
                    + "' AND "+TaskContract.Columns.HUE+" < '" + task_max+"'"
                    + " AND "+TaskContract.Columns.SATURATION+" = '" + task_sat+"'"
                    + " AND "+TaskContract.Columns.VALUE+" = '" + task_val+"'"
            ;
        }


        String[] PROJECTION = { TaskContract.Columns.NAME, TaskContract.Columns.HUE, TaskContract.Columns.SATURATION, TaskContract.Columns.VALUE };

        if (task_min.length() == 0 || task_max.length() == 0 || hue_1 < 0.0f || hue_1 > 360.0f || hue_2 < 0.0f || hue_2 > 360.0f) {
            Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_SHORT).show();
        }
        else {
            cursor = getActivity().getContentResolver().query(uri, PROJECTION, searchQuery, null,null);
            if (cursor != null) {
                if(cursor.getCount()==0){
                    list_name.add("No entry in database have the following HSV values:\n"
                                    +"Hues :"+task_min +"° - "+task_max+"°\n"
                                    + "Saturation :"+ task_sat+"%\n"
                                    + "Value :"+ task_val+"%\n"
                    );
                }
                else{
                    while (cursor.moveToNext()) {
                        list_name.add(cursor.getString(0));
                        list_hue.add(cursor.getString(1));
                        list_saturation.add(cursor.getString(2));
                        list_value.add(cursor.getString(3));
                    }
                    Sorted_values();
                }
            }


        }

        basecolor_listview_4 = (ListView) rootView.findViewById(R.id.basecolor_listview_1);
        basecolor_listview_4.setAdapter(new ColorNameAdapter(getActivity(),list_name));
        basecolor_listview_4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "Hue: " +list_hue.get(position) + "° " +" \n Saturation: " + list_saturation.get(position) + "%"+"\n Value: " + list_value.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }


    private void Clear_list() {
        list_name.clear();
        list_hue.clear();
        list_saturation.clear();
        list_value.clear();
    }


    public class Sorted {

        public String Name;
        public int Hue;
        public int Saturation;
        public int Value;

        public Sorted(String _name, int _hue, int _saturation, int _value) {
            Name = _name;
            Hue = _hue;
            Saturation = _saturation;
            Value = _value;
        }

    }

    private void Sorted_values() {

        ArrayList<Sorted> list_sorted = new ArrayList<Sorted>();
        list_sorted_value.clear();
        list_sorted.clear();
        for (int i = 0; i < list_name.size(); i++) {
            Sorted obj_stored = new Sorted(list_name.get(i), Integer.valueOf(list_hue.get(i)), Integer.valueOf(list_saturation.get(i)), Integer.valueOf(list_value.get(i)));
            list_sorted.add(obj_stored);
        }
        for (int i = 0; i < list_name.size(); i++) {
            list_sorted_value.add(list_sorted.get(i).Name + "     H:" + String.valueOf(list_sorted.get(i).Hue) + "     S:" + String.valueOf(list_sorted.get(i).Saturation) + "     V:" + String.valueOf(list_sorted.get(i).Value));
        }
    }




}
