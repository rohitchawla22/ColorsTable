package id.color.colorstable;


import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri uri = new TaskContract().CONTENT_URI;
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();

        Resources resources = getResources();
        String[] color_name = resources.getStringArray(R.array.color_name);
        String[] color_hue = resources.getStringArray(R.array.color_hue);
        String[] color_saturation = resources.getStringArray(R.array.color_saturation);
        String[] color_value = resources.getStringArray(R.array.color_value);
        String selection = TaskContract.Columns.HUE + ">?";
        Cursor cursor = getContentResolver().query(uri, null, selection, new String[]{"0"}, null);
        if(cursor!=null && cursor.getCount()>0){
        }
        else {
            for (int i = 0; i < resources.getStringArray(R.array.color_name).length; i++) {
                values.put(TaskContract.Columns.NAME, color_name[i]);
                values.put(TaskContract.Columns.HUE, color_hue[i]);
                values.put(TaskContract.Columns.SATURATION, color_saturation[i]);
                values.put(TaskContract.Columns.VALUE, color_value[i]);
                getApplicationContext().getContentResolver().insert(uri, values);
            }
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction ();
        Fragment myFrag = new Fragment1();
        fragmentTransaction.add(R.id.listFragment, myFrag);
        fragmentTransaction.commit ();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

