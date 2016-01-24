package id.color.colorstable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SearchFragment extends Fragment {
    private int sorted_value = 0;


    private Button button_hue, button_value, button_saturation, button_name,button_all,button_sort;
    private EditText edit_hue, edit_value, edit_saturation_min, edit_saturation_max, edit_name,edit_saturation_all, edit_value_all;
    private AutoCompleteTextView edit_text_complete;

    private ListView search_list;

    private Uri uri = TaskContract.CONTENT_URI;
    private AlertDialog.Builder dialog;

    SharedPreferences mPrefs;

    private ArrayList<String> list_name = new ArrayList<>();
    private ArrayList<String> list_hue = new ArrayList<>();
    private ArrayList<String> list_saturation = new ArrayList<>();
    private ArrayList<String> list_value = new ArrayList<>();
    private ArrayList<String> list_sorted_value = new ArrayList<>();

    Cursor cursor;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.searchcolor_fragment, container, false);
        mPrefs = getActivity().getSharedPreferences("MyPerf", 0);
        sorted_value = mPrefs.getInt("sorted_value", 0);
        Resources resources = getResources();

        search_list = (ListView)rootView.findViewById(R.id.search_listview);
        String selection = TaskContract.Columns.HUE + ">=?";
        cursor = getActivity().getContentResolver().query(uri, null, selection, new String[]{"0"}, null);
        if (cursor != null) {
                while (cursor.moveToNext()) {
                    list_name.add(cursor.getString(1));
                    list_hue.add(cursor.getString(2));
                    list_saturation.add(cursor.getString(3));
                    list_value.add(cursor.getString(4));
                }
            }
            Sorted_values();
            search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));




        edit_hue = (EditText)rootView.findViewById(R.id.edit_hue);
        button_hue = (Button)rootView.findViewById(R.id.button_hue);
        button_hue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_list();
                String selection = TaskContract.Columns.HUE + "=?";
                String task = edit_hue.getText().toString();
                if (task.length() == 0 || Integer.valueOf(task) < 0 || Integer.valueOf(task) > 360) {
                    Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_SHORT).show();
                }
                else {
                    cursor = getActivity().getContentResolver().query(uri, null, selection, new String[]{task}, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            list_name.add(cursor.getString(1));
                            list_hue.add(cursor.getString(2));
                            list_saturation.add(cursor.getString(3));
                            list_value.add(cursor.getString(4));
                        }
                    }
                    Clear_listview();
                    Sorted_values();
                    search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));
                }

            }
        });

        edit_value = (EditText)rootView.findViewById(R.id.edit_value);
        button_value = (Button)rootView.findViewById(R.id.button_value);
        button_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_list();
                String selection =TaskContract.Columns.VALUE + "<=?";
                String task = edit_value.getText().toString();
                if (task.length() == 0 || Integer.valueOf(task) < 0 || Integer.valueOf(task) > 100) {
                    Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_SHORT).show();
                }
                else {
                    cursor = getActivity().getContentResolver().query(uri, null, selection, new String[]{task}, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            list_name.add(cursor.getString(1));
                            list_hue.add(cursor.getString(2));
                            list_saturation.add(cursor.getString(3));
                            list_value.add(cursor.getString(4));
                        }
                    }
                    Clear_listview();
                    Sorted_values();
                    search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));
                }
            }
        });

        edit_saturation_min = (EditText)rootView.findViewById(R.id.edit_min_saturation);
        edit_saturation_max = (EditText)rootView.findViewById(R.id.edit_max_saturation);
        button_saturation = (Button)rootView.findViewById(R.id.button_saturation);
        button_saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_list();
                String task_min = edit_saturation_min.getText().toString();
                String task_max = edit_saturation_max.getText().toString();
                String selection =  TaskContract.Columns.SATURATION+" >= '" + task_min
                        + "' AND "+TaskContract.Columns.SATURATION+" <= '" + task_max+"'"
                        ;
                if (task_min.length() == 0 || task_max.length() == 0 || Integer.valueOf(task_min) < 0 || Integer.valueOf(task_min) > 100 || Integer.valueOf(task_max) < 0 || Integer.valueOf(task_max) > 100) {
                    Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_SHORT).show();
                }
                else {
                    cursor = getActivity().getContentResolver().query(uri, null, selection, null,null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            list_name.add(cursor.getString(1));
                            list_hue.add(cursor.getString(2));
                            list_saturation.add(cursor.getString(3));
                            list_value.add(cursor.getString(4));
                        }
                    }
                    Clear_listview();
                    Sorted_values();
                    search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));
                }

            }
        });




        edit_name = (EditText)rootView.findViewById(R.id.color_name);
        button_name = (Button)rootView.findViewById(R.id.button_search);
        button_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Clear_list();
                String name = edit_name.getText().toString();
                final String sa1 = "%"+name+"%";
                cursor = getActivity().getContentResolver().query(uri, null, TaskContract.Columns.NAME+ " LIKE ?", new String[] { sa1 }, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            list_name.add(cursor.getString(1));
                            list_hue.add(cursor.getString(2));
                            list_saturation.add(cursor.getString(3));
                            list_value.add(cursor.getString(4));
                        }
                    }
                    Clear_listview();
                    Sorted_values();
                    search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));



            }
        });

        edit_text_complete = (AutoCompleteTextView)rootView.findViewById(R.id.edit_name_complete);
        edit_text_complete.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.name_base_color)));
        edit_text_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (Check_hue(edit_text_complete.getText().toString())) {
                    case 0 :
                        Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        Clear_list();
                        List_upload("0", "30");
                        List_upload("330", "360");
                        break;
                    case 2 :
                        Clear_list();
                        List_upload("30", "90");
                        break;
                    case 3 :
                        Clear_list();
                        List_upload("90", "150");
                        break;
                    case 4 :
                        Clear_list();
                        List_upload("150", "210");
                        break;
                    case 5 :
                        Clear_list();
                        List_upload("210", "270");
                        break;
                    case 6 :
                        Clear_list();
                        List_upload("270", "330");
                        break;
                }
            }
        });


        edit_saturation_all = (EditText)rootView.findViewById(R.id.edit_saturation_complete);
        edit_value_all = (EditText)rootView.findViewById(R.id.edit_value_complete);
        button_all = (Button)rootView.findViewById(R.id.button_search_all);
        button_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saturation_min = String.valueOf(Integer.valueOf(edit_saturation_all.getText().toString()) - 5);
                String saturation_max = String.valueOf(Integer.valueOf(edit_saturation_all.getText().toString()) + 5);
                String value = edit_value_all.getText().toString();
                switch (Check_hue(edit_text_complete.getText().toString())) {
                    case 0 :
                        Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        Clear_list();
                        List_upload_all("330", "360", saturation_min, saturation_max, value);
                        List_upload_all("0", "30", saturation_min, saturation_max, value);
                        break;
                    case 2 :
                        Clear_list();
                        List_upload_all("30", "90", saturation_min, saturation_max, value);
                        break;
                    case 3 :
                        Clear_list();
                        List_upload_all("90", "150", saturation_min, saturation_max, value);
                        break;
                    case 4 :
                        Clear_list();
                        List_upload_all("150", "210", saturation_min, saturation_max, value);
                        break;
                    case 5 :
                        Clear_list();
                        List_upload_all("210", "270", saturation_min, saturation_max, value);
                        break;
                    case 6 :
                        Clear_list();
                        List_upload_all("270", "330", saturation_min, saturation_max, value);
                        break;
                }
            }
        });
        button_sort = (Button)rootView.findViewById(R.id.change_sort);
        button_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] mChoose = { "Hue, Saturation, Value", "Hue, Value, Saturation", "Saturation, Hue, Value", "Saturation, Value, Hue", "Value, Hue, Saturation", "Value, Saturation, Hue"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sorted by")
                        .setCancelable(false)
                        .setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setSingleChoiceItems(mChoose, mPrefs.getInt("sorted_value", 0), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                sorted_value = item;
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putInt("sorted_value", item).commit();
                                Clear_listview();
                                Sorted_values();
                                search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });



        return rootView;
    }

    private void Clear_listview() {
        String[] array_null = {};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array_null);
        search_list.setAdapter(adapter);
    }

    private void Clear_list() {
        list_name.clear();
        list_hue.clear();
        list_saturation.clear();
        list_value.clear();
    }


    private int Check_hue(String _value) {
        int check_value = 0;
        if (_value.equalsIgnoreCase("Red")) {
            check_value = 1;
        }
        if (_value.equalsIgnoreCase("Yellow")) {
            check_value = 2;
        }
        if (_value.equalsIgnoreCase("Green")) {
            check_value = 3;
        }
        if (_value.equalsIgnoreCase("Cyan")) {
            check_value = 4;
        }
        if (_value.equalsIgnoreCase("Blue")) {
            check_value = 5;
        }
        if (_value.equalsIgnoreCase("Magenta")) {
            check_value = 6;
        }
        return check_value;
    }

    private void List_upload(String _value_min, String _value_max) {

        String selection = TaskContract.Columns.HUE + ">=?" + " AND " + TaskContract.Columns.HUE + "<=?";
        String task_min = _value_min;
        String task_max = _value_max;
        cursor = getActivity().getContentResolver().query(uri, null, selection, new String[]{task_min, task_max}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list_name.add(cursor.getString(1));
                list_hue.add(cursor.getString(2));
                list_saturation.add(cursor.getString(3));
                list_value.add(cursor.getString(4));
            }
        }
        Clear_listview();
        Sorted_values();
        search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));
        Log.i("NAME", list_name.toString());
        Log.i("HUE", list_hue.toString());
        Log.i("SATURATION", list_saturation.toString());
        Log.i("VALUE", list_value.toString());
    }

    private void List_upload_all(String _hue_min, String _hue_max, String _saturation_min, String _saturation_max, String _value) {

        String selection =  TaskContract.Columns.HUE+" >= '" + _hue_min
                + "' AND "+TaskContract.Columns.HUE+" <= '" + _hue_max
                + "' AND "+TaskContract.Columns.SATURATION+" >= '" + _saturation_min
                + "' AND "+TaskContract.Columns.SATURATION+" <= '" + _saturation_max
                + "' AND "+TaskContract.Columns.VALUE+" >= '" + _value+"'"
                ;
        if (_saturation_min.length() == 0 || _saturation_max.length() == 0 || Integer.valueOf(_saturation_min) < 0 || Integer.valueOf(_saturation_min) > 100 || Integer.valueOf(_saturation_max) < 0 || Integer.valueOf(_saturation_max) > 100 || _value.length() == 0 || Integer.valueOf(_value) < 0 || Integer.valueOf(_value) > 100) {
            Toast.makeText(getActivity(), "Invalid value", Toast.LENGTH_SHORT).show();
        }
        else {
            cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list_name.add(cursor.getString(1));
                    list_hue.add(cursor.getString(2));
                    list_saturation.add(cursor.getString(3));
                    list_value.add(cursor.getString(4));
                }
            }
            Clear_listview();
            Sorted_values();
            search_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list_sorted_value));
        }
        Log.i("NAME", list_name.toString());
        Log.i("HUE", list_hue.toString());
        Log.i("SATURATION", list_saturation.toString());
        Log.i("VALUE", list_value.toString());
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
        switch (sorted_value) {
            case 0 :
                list_sorted_value.clear();
                list_sorted.clear();
                for (int i = 0; i < list_name.size(); i++) {
                    Sorted obj_stored = new Sorted(list_name.get(i), Integer.valueOf(list_hue.get(i)), Integer.valueOf(list_saturation.get(i)), Integer.valueOf(list_value.get(i)));
                    list_sorted.add(obj_stored);
                }
                Log.i("SORTED", "0");
                Collections.sort(list_sorted, new Comparator<Sorted>() {
                    @Override
                    public int compare(Sorted x_1, Sorted x_2) {
                        if (x_1.Hue > x_2.Hue) {
                            return 1;
                        }
                        else if(x_1.Hue < x_2.Hue)
                            return -1;
                        else{
                            if (x_1.Saturation > x_2.Saturation) {
                                return 1;
                            }
                            else if (x_1.Saturation < x_2.Saturation)
                                return -1;
                            else {
                                if (x_1.Value < x_2.Value) {
                                    return -1;
                                }
                                else {
                                    return 1;
                                }
                            }
                        }
                    }
                });
                for (int i = 0; i < list_name.size(); i++) {
                    list_sorted_value.add(list_sorted.get(i).Name + "     H:" + String.valueOf(list_sorted.get(i).Hue) + "     S:" + String.valueOf(list_sorted.get(i).Saturation) + "     V:" + String.valueOf(list_sorted.get(i).Value));
                }
                break;
            case 1 :
                list_sorted_value.clear();
                list_sorted.clear();
                for (int i = 0; i < list_name.size(); i++) {
                    Sorted obj_stored = new Sorted(list_name.get(i), Integer.valueOf(list_hue.get(i)), Integer.valueOf(list_saturation.get(i)), Integer.valueOf(list_value.get(i)));
                    list_sorted.add(obj_stored);
                }
                Log.i("SORTED", "1");
                Collections.sort(list_sorted, new Comparator<Sorted>() {
                    @Override
                    public int compare(Sorted x_1, Sorted x_2) {
                        if (x_1.Hue > x_2.Hue) {
                            return 1;
                        }
                        else if(x_1.Hue < x_2.Hue)
                            return -1;
                        else{
                            if (x_1.Value > x_2.Value) {
                                return 1;
                            }
                            else if (x_1.Value < x_2.Value)
                                return -1;
                            else {
                                if (x_1.Saturation < x_2.Saturation) {
                                    return -1;
                                }
                                else {
                                    return 1;
                                }
                            }
                        }
                    }
                });
                for (int i = 0; i < list_name.size(); i++) {
                    list_sorted_value.add(list_sorted.get(i).Name + "     H:" + String.valueOf(list_sorted.get(i).Hue) + "     S:" + String.valueOf(list_sorted.get(i).Saturation) + "     V:" + String.valueOf(list_sorted.get(i).Value));
                }
                break;
            case 2 :
                list_sorted_value.clear();
                list_sorted.clear();
                for (int i = 0; i < list_name.size(); i++) {
                    Sorted obj_stored = new Sorted(list_name.get(i), Integer.valueOf(list_hue.get(i)), Integer.valueOf(list_saturation.get(i)), Integer.valueOf(list_value.get(i)));
                    list_sorted.add(obj_stored);
                }
                Log.i("SORTED", "2");
                Collections.sort(list_sorted, new Comparator<Sorted>() {
                    @Override
                    public int compare(Sorted x_1, Sorted x_2) {
                        if (x_1.Saturation > x_2.Saturation) {
                            return 1;
                        }
                        else if(x_1.Saturation < x_2.Saturation)
                            return -1;
                        else{
                            if (x_1.Hue > x_2.Hue) {
                                return 1;
                            }
                            else if (x_1.Hue < x_2.Hue)
                                return -1;
                            else {
                                if (x_1.Value < x_2.Value) {
                                    return -1;
                                }
                                else {
                                    return 1;
                                }
                            }
                        }
                    }
                });
                for (int i = 0; i < list_name.size(); i++) {
                    list_sorted_value.add(list_sorted.get(i).Name + "     H:" + String.valueOf(list_sorted.get(i).Hue) + "     S:" + String.valueOf(list_sorted.get(i).Saturation) + "     V:" + String.valueOf(list_sorted.get(i).Value));
                }
                break;
            case 3 :
                list_sorted_value.clear();
                list_sorted.clear();
                for (int i = 0; i < list_name.size(); i++) {
                    Sorted obj_stored = new Sorted(list_name.get(i), Integer.valueOf(list_hue.get(i)), Integer.valueOf(list_saturation.get(i)), Integer.valueOf(list_value.get(i)));
                    list_sorted.add(obj_stored);
                }
                Log.i("SORTED", "3");
                Collections.sort(list_sorted, new Comparator<Sorted>() {
                    @Override
                    public int compare(Sorted x_1, Sorted x_2) {
                        if (x_1.Saturation > x_2.Saturation) {
                            return 1;
                        }
                        else if(x_1.Saturation < x_2.Saturation)
                            return -1;
                        else{
                            if (x_1.Value > x_2.Value) {
                                return 1;
                            }
                            else if (x_1.Value < x_2.Value)
                                return -1;
                            else {
                                if (x_1.Hue < x_2.Hue) {
                                    return -1;
                                }
                                else {
                                    return 1;
                                }
                            }
                        }
                    }
                });
                for (int i = 0; i < list_name.size(); i++) {
                    list_sorted_value.add(list_sorted.get(i).Name + "     H:" + String.valueOf(list_sorted.get(i).Hue) + "     S:" + String.valueOf(list_sorted.get(i).Saturation) + "     V:" + String.valueOf(list_sorted.get(i).Value));
                }
                break;
            case 4 :
                list_sorted_value.clear();
                list_sorted.clear();
                for (int i = 0; i < list_name.size(); i++) {
                    Sorted obj_stored = new Sorted(list_name.get(i), Integer.valueOf(list_hue.get(i)), Integer.valueOf(list_saturation.get(i)), Integer.valueOf(list_value.get(i)));
                    list_sorted.add(obj_stored);
                }
                Log.i("SORTED", "4");
                Collections.sort(list_sorted, new Comparator<Sorted>() {
                    @Override
                    public int compare(Sorted x_1, Sorted x_2) {
                        if (x_1.Value > x_2.Value) {
                            return 1;
                        }
                        else if(x_1.Value < x_2.Value)
                            return -1;
                        else{
                            if (x_1.Hue > x_2.Hue) {
                                return 1;
                            }
                            else if (x_1.Hue < x_2.Hue)
                                return -1;
                            else {
                                if (x_1.Saturation < x_2.Saturation) {
                                    return -1;
                                }
                                else {
                                    return 1;
                                }
                            }
                        }
                    }
                });
                for (int i = 0; i < list_name.size(); i++) {
                    list_sorted_value.add(list_sorted.get(i).Name + "     H:" + String.valueOf(list_sorted.get(i).Hue) + "     S:" + String.valueOf(list_sorted.get(i).Saturation) + "     V:" + String.valueOf(list_sorted.get(i).Value));
                }
                break;
            case 5 :
                list_sorted_value.clear();
                list_sorted.clear();
                for (int i = 0; i < list_name.size(); i++) {
                    Sorted obj_stored = new Sorted(list_name.get(i), Integer.valueOf(list_hue.get(i)), Integer.valueOf(list_saturation.get(i)), Integer.valueOf(list_value.get(i)));
                    list_sorted.add(obj_stored);
                }
                Log.i("SORTED", "5");
                Collections.sort(list_sorted, new Comparator<Sorted>() {
                    @Override
                    public int compare(Sorted x_1, Sorted x_2) {
                        if (x_1.Value > x_2.Value) {
                           return 1;
                        }
                        else if(x_1.Value < x_2.Value)
                            return -1;
                        else{
                            if (x_1.Saturation > x_2.Saturation) {
                               return 1;
                            }
                            else if (x_1.Saturation < x_2.Saturation)
                                return -1;
                            else {
                                if (x_1.Hue < x_2.Hue) {
                                    return -1;
                                }
                                else {
                                    return 1;
                                }
                            }
                        }
                        }


                });
                for (int i = 0; i < list_name.size(); i++) {
                    list_sorted_value.add(list_sorted.get(i).Name + "     H:" + String.valueOf(list_sorted.get(i).Hue) + "     S:" + String.valueOf(list_sorted.get(i).Saturation) + "     V:" + String.valueOf(list_sorted.get(i).Value));
                }
                break;
        }


    }

}
