package id.color.colorstable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;


public class ColorAdapter extends BaseAdapter {

    private ArrayList<Integer> list_color_1 = new ArrayList<>();
    private ArrayList<Integer> list_color_2 = new ArrayList<>();
    private Context context;
    private ImageView base_imageview;

    public ColorAdapter(Context _context, ArrayList<Integer> _list_color_1, ArrayList<Integer> _list_color_2) {
        this.context = _context;
        this.list_color_1 = _list_color_1;
        this.list_color_2 = _list_color_2;
    }

    @Override
    public int getCount() {
        return list_color_1.size();
    }

    @Override
    public Object getItem(int position) {
        return list_color_1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.base_listview, null);

        base_imageview = (ImageView)convertView.findViewById(R.id.base_imageview);

        float[] pixelHSV= new float[3];
        float[] pixelHSV1= new float[3];
        float[] pixelHSV2 = new float[3];
        Color.colorToHSV(list_color_1.get(position), pixelHSV1);
        Color.colorToHSV(list_color_2.get(position),pixelHSV2);
        int difference=0;
        if(pixelHSV1[0] > pixelHSV2[0])
            difference = (int)(360.0f-pixelHSV1[0] +pixelHSV2[0]);
        else
            difference = Math.abs((int)(pixelHSV1[0]- pixelHSV2[0]));
        int step =0;

        if(difference==0)
            difference=360;
        if(difference<50)
            step =2;
        else if(difference>=50 && difference<100)
            step =5;
        else if(difference>=100 && difference<200)
            step =8;
        else
            step = 10;

        int interval =difference/step;
        int[] array = new int[step];

        for(int i=0;i<array.length;i++){
            if(i==0)
                array[i]= Color.HSVToColor(pixelHSV1);
            else{
                float h0 =pixelHSV1[0];
                float h1 =pixelHSV1[1];
                float h2 =pixelHSV1[2];
                pixelHSV[0]= h0;
                pixelHSV[1]= h1;
                pixelHSV[2]= h2;

                pixelHSV[0]=h0;
                pixelHSV[0]=pixelHSV[0]+ i* interval;
                if( pixelHSV[0]>360 &&  pixelHSV[0]<=720)
                    pixelHSV[0]= pixelHSV[0]-360;
                else if( pixelHSV[0]>720)
                    pixelHSV[0]= pixelHSV[0]-720;
                array[i]=   Color.HSVToColor(pixelHSV);
            }
        }

        array[array.length-1] = Color.HSVToColor(pixelHSV2);
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, array);
        base_imageview.setBackground(gradient);


        return convertView;
    }
}
