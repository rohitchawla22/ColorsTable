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


public class Fragment3 extends Fragment {

    private float hue_1, hue_2, saturation_1, saturation_2;
    private ListView basecolor_listview_3;
    private ArrayList<Integer> list_color_1;
    private ArrayList<Integer> list_color_2 ;
    private ArrayList<Float> list_change_1 ;
    private ArrayList<Float> list_change_2;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    SharedPreferences mPrefsv;
    private  int numOfSwatchesv=18;
    AlertDialog ad;

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
            list_color_1 = new ArrayList<>();
            list_color_2 = new ArrayList<>();
            list_change_1 = new ArrayList<>();
            list_change_2 = new ArrayList<>();
            final View rootView = inflater.inflate(R.layout.base_color, container, false);

            mPrefsv = getActivity().getSharedPreferences( "MyPerf3", Context.MODE_PRIVATE);
            numOfSwatchesv = mPrefsv.getInt("numOfSwatchesv",18);
            Button confButtonH = (Button) rootView.findViewById(R.id.confButton);
            confButtonH.setVisibility(View.INVISIBLE);
            confButtonH.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            Button confButtonv = (Button) rootView.findViewById(R.id.searchButton);
            confButtonv.setText("Configuration");

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                hue_1 = bundle.getFloat("hue_1", 0);
                hue_2 = bundle.getFloat("hue_2", 0);
                saturation_1 = bundle.getFloat("saturation_1", 0);
                saturation_2 = bundle.getFloat("saturation_2", 0);
            }



            basecolor_listview_3 = (ListView) rootView.findViewById(R.id.basecolor_listview_1);
            UpLoadColor();
            basecolor_listview_3.setAdapter(new ColorAdapter(getActivity(), list_color_1, list_color_2));
            basecolor_listview_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction ();
                    Fragment myFrag = new Fragment4();
                    Bundle bundle = new Bundle();
                    bundle.putFloat("hue_1", hue_1);
                    bundle.putFloat("hue_2", hue_2);
                    bundle.putFloat("saturation", (int) (saturation_1 * 100));
                    bundle.putFloat("values", (int) (list_change_1.get(position) * 100));
                    myFrag.setArguments(bundle);
                    fragmentTransaction.replace(R.id.listFragment, myFrag).addToBackStack("fragment4");
                    fragmentTransaction.commit ();

                }
            });

            confButtonv.setOnClickListener(new View.OnClickListener() {
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
                    seek.setProgress(numOfSwatchesv);
                    final TextView num = new TextView(getActivity());
                    num.setText("Number of swatches : "+ numOfSwatchesv);
                    num.setGravity(Gravity.CENTER);

                    seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            numOfSwatchesv =progress;
                            num.setText("Number of swatches : "+ numOfSwatchesv);

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
                            SharedPreferences.Editor mEditor = mPrefsv.edit();
                            mEditor.putInt("numOfSwatchesv", numOfSwatchesv).commit();
                            UpLoadColor();
                            basecolor_listview_3.setAdapter(new ColorAdapter(getActivity(), list_color_1, list_color_2));
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

        double intervalv = 1.0/numOfSwatchesv;
        DecimalFormat df = new DecimalFormat("#.##");
        df.format(intervalv);

        float[] hsv = new float[3];
        for (double i = 1; i > intervalv/2; i -= intervalv) {
            hsv[0] =  hue_1;
            hsv[1] =  saturation_1;
            hsv[2] = (float) i;
            list_color_1.add(Color.HSVToColor(hsv));
            list_change_1.add(hsv[2]);
        }
        for (double i = 1; i > intervalv/2; i -= intervalv) {
            hsv[0] = hue_2;
            hsv[1] = saturation_2;
            hsv[2] = (float) i;
            list_color_2.add(Color.HSVToColor(hsv));
            list_change_2.add(hsv[2]);
        }
    }

    }
