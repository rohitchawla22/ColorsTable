package id.color.colorstable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class Fragment1 extends Fragment {
    private Button searchButton;
    private ListView basecolor_listview_1;
    private ArrayList<Integer> list_color_1;
    private ArrayList<Integer> list_color_2 ;
    private ArrayList<Float> list_change_1 ;
    private ArrayList<Float> list_change_2;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    private Button huesConfig ;
    SharedPreferences mPrefs;
    private  int numOfSwatchesh=4;
    private  int centerOfHues=120;
    AlertDialog ad;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        list_color_1 = new ArrayList<>();
        list_color_2 = new ArrayList<>();
        list_change_1 = new ArrayList<>();
        list_change_2 = new ArrayList<>();

        final View rootView = inflater.inflate(R.layout.base_color, container, false);
        mPrefs = getActivity().getSharedPreferences("MyPerf4", Context.MODE_PRIVATE);
        numOfSwatchesh = mPrefs.getInt("numOfSwatchesh",4);
        centerOfHues = mPrefs.getInt("centerOfHues",120);


        huesConfig = (Button) rootView.findViewById(R.id.confButton);

        huesConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Configure color swatches");
                alert.setMessage("Number of swatches to display ");

                LinearLayout linear=new LinearLayout(getActivity());

                linear.setOrientation(LinearLayout.VERTICAL);
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                final ImageView img =new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(height/10, height/10);
                layoutParams.gravity=Gravity.CENTER;
                img.setLayoutParams(layoutParams);
                img.setPadding(0, 35, 0, 35);
                float[] hsv = new float[3];
                hsv[0]= centerOfHues;
                hsv[1]= 1.0f;
                hsv[2]= 1.0f;
                img.setBackgroundColor(Color.HSVToColor(hsv));


                SeekBar seek=new SeekBar(getActivity());
                seek.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f));
                seek.setMax(360);
                seek.setPadding(0,20,0,0);
                seek.setProgress(centerOfHues);

                final TextView num = new TextView(getActivity());
                num.setText("Center of hues : "+ centerOfHues);
                num.setGravity(Gravity.CENTER);
                num.setPadding(0, 0, 0, 35);


                SeekBar seek1=new SeekBar(getActivity());
                seek1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f));
                seek1.setMax(35);
                seek1.setProgress(numOfSwatchesh);

                final TextView num1 = new TextView(getActivity());
                num1.setText("Number of swatches : "+ numOfSwatchesh);
                num1.setGravity(Gravity.CENTER);
                num1.setPadding(0,0,0,35);

                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        centerOfHues =progress;
                        num.setText("Center of hues : "+ centerOfHues);
                        float[] hsv = new float[3];
                        hsv[0]= centerOfHues;
                        hsv[1]= 1.0f;
                        hsv[2]= 1.0f;
                        img.setBackgroundColor(Color.HSVToColor(hsv));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        numOfSwatchesh =progress+1;
                        num1.setText("Number of swatches : "+ numOfSwatchesh);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                linear.addView(img);
                linear.addView(seek);
                linear.addView(num);
                linear.addView(seek1);
                linear.addView(num1);
                alert.setView(linear);

                alert.setPositiveButton("Ok",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putInt("numOfSwatchesh", numOfSwatchesh).commit();
                        mEditor.putInt("centerOfHues", centerOfHues).commit();
                        UpLoadColor();
                        basecolor_listview_1.setAdapter(new ColorAdapter(getActivity(), list_color_1, list_color_2));
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


        basecolor_listview_1 = (ListView) rootView.findViewById(R.id.basecolor_listview_1);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction ();
                Fragment myFrag = new SearchFragment();
                fragmentTransaction.replace(R.id.listFragment, myFrag).addToBackStack("search");
                fragmentTransaction.commit ();
            }
        });

        UpLoadColor();
        basecolor_listview_1.setAdapter(new ColorAdapter(getActivity(), list_color_1, list_color_2));
        basecolor_listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction ();
                Fragment myFrag = new Fragment2();
                Bundle bundle = new Bundle();
                bundle.putFloat("hue_1", list_change_1.get(position));
                bundle.putFloat("hue_2", list_change_2.get(position));
                myFrag.setArguments(bundle);
                fragmentTransaction.replace(R.id.listFragment, myFrag).addToBackStack("fragment2");
                fragmentTransaction.commit ();
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
        double interval = 360.0/numOfSwatchesh;
        for (int i = 0; i < numOfSwatchesh; i ++) {
            hsv[0] = (float) (centerOfHues + (i*interval) -(interval/2));
            if(hsv[0]>360 && hsv[0]<=720)
                hsv[0]= hsv[0]-360;
            else  if(hsv[0]>720 )
                hsv[0]= hsv[0]-720;
            if(hsv[0]<0 && hsv[0]>=-360)
                hsv[0]= 360- Math.abs(hsv[0]);
            else  if(hsv[0]< -360 )
                hsv[0]= 720- Math.abs(hsv[0]);
            hsv[1] = (float) 1.0;
            hsv[2] = (float) 1.0;
            list_color_1.add(Color.HSVToColor(hsv));
            list_change_1.add(hsv[0]);

        }
        for (int i = 0; i < numOfSwatchesh; i ++) {
            hsv[0] = (float) (centerOfHues + (i*interval)+(interval/2));
            if(hsv[0]>360 && hsv[0]<=720)
                hsv[0]= hsv[0]-360;
            else  if(hsv[0]>720 )
                hsv[0]= hsv[0]-720;
            if(hsv[0]<0 && hsv[0]>=-360)
                hsv[0]= 360- Math.abs(hsv[0]);
            else  if(hsv[0]< -360 )
                hsv[0]= 720- Math.abs(hsv[0]);
            hsv[1] = (float) 1.0;
            hsv[2] = (float) 1.0;
            list_color_2.add(Color.HSVToColor(hsv));
            list_change_2.add(hsv[0]);

        }
    }
}


