package id.color.colorstable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Fragment2 extends Fragment {

    private float hue_1, hue_2;

    private ListView basecolor_listview_2;
    private ArrayList<Integer> list_color_1;
    private ArrayList<Integer> list_color_2 ;
    private ArrayList<Float> list_change_1 ;
    private ArrayList<Float> list_change_2;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    SharedPreferences mPrefs;
    private  int numOfSwatches=18;
    AlertDialog ad;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        list_color_1 = new ArrayList<>();
        list_color_2 = new ArrayList<>();
        list_change_1 = new ArrayList<>();
        list_change_2 = new ArrayList<>();

        final View rootView = inflater.inflate(R.layout.base_color, container, false);

        mPrefs = getActivity().getSharedPreferences("MyPerf1", Context.MODE_PRIVATE);
        numOfSwatches = mPrefs.getInt("numOfSwatches",18);
        Button confButtonH = (Button) rootView.findViewById(R.id.confButton);
        confButtonH.setVisibility(View.INVISIBLE);
        confButtonH.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        Button confButton = (Button) rootView.findViewById(R.id.searchButton);
        confButton.setText("Configuration");


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            hue_1 = bundle.getFloat("hue_1", 0);
            hue_2 = bundle.getFloat("hue_2", 0);
        }

        basecolor_listview_2 = (ListView) rootView.findViewById(R.id.basecolor_listview_1);
        UpLoadColor();
        basecolor_listview_2.setAdapter(new ColorAdapter(getActivity(), list_color_1, list_color_2));
        basecolor_listview_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction ();
                Fragment myFrag = new Fragment3();
                Bundle bundle = new Bundle();
                bundle.putFloat("hue_1", hue_1);
                bundle.putFloat("hue_2", hue_2);
                bundle.putFloat("saturation_1", list_change_1.get(position));
                bundle.putFloat("saturation_2", list_change_2.get(position));
                myFrag.setArguments(bundle);
                fragmentTransaction.replace(R.id.listFragment, myFrag).addToBackStack("fragment3");
                fragmentTransaction.commit ();
            }
        });


        confButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Configure color swatches");
                alert.setMessage("Number of swatches to display ");
                LinearLayout linear=new LinearLayout(getActivity());
                linear.setOrientation(LinearLayout.VERTICAL);
                SeekBar seek=new SeekBar(getActivity());
                seek.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,1.0f));
                seek.setMax(100);
                seek.setProgress(numOfSwatches);
                final TextView num = new TextView(getActivity());
                num.setText("Number of swatches : "+ numOfSwatches);
                num.setGravity(Gravity.CENTER);

                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        numOfSwatches =progress;
                        num.setText("Number of swatches : "+ numOfSwatches);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                linear.addView(seek);
                linear.addView(num);
                alert.setView(linear);
                alert.setPositiveButton("Ok",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putInt("numOfSwatches", numOfSwatches).commit();
                        UpLoadColor();
                        basecolor_listview_2.setAdapter(new ColorAdapter(getActivity(), list_color_1, list_color_2));
                    }
                });

                alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        ad.dismiss();
                    }
                });
                ad = alert.show();
            }
        });




        return rootView;
    }

    private final void UpLoadColor() {
        list_color_1 = new ArrayList<>();
        list_color_2 = new ArrayList<>();
        list_change_1 = new ArrayList<>();
        list_change_2 = new ArrayList<>();

        float[] hsv = new float[3];
        double interval = 1.0/numOfSwatches;
        DecimalFormat df = new DecimalFormat("#.##");
        df.format(interval);
        for (double i = 1; i >= interval/2; i -= interval) {
            hsv[0] =  hue_1;
            hsv[1] = (float) i;
            hsv[2] = (float) 1.0;
            list_color_1.add(Color.HSVToColor(hsv));
            list_change_1.add(hsv[1]);
        }
        for (double i = 1; i >= interval/2; i -= interval) {
            hsv[0] = hue_2;
            hsv[1] = (float) i;
            hsv[2] = (float) 1.0;
            list_color_2.add(Color.HSVToColor(hsv));
            list_change_2.add(hsv[1]);
        }
    }



}
