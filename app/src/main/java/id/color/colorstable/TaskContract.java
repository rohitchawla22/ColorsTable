package id.color.colorstable;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by EMAN on 4/23/2015.
 */
public class TaskContract {
    public static final String DB_NAME = "db_colortable";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "colors";
    public static final String AUTHORITY = "color.colortable";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int TASKS_LIST = 1;
    public static final int TASKS_ITEM = 2;
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/example.colors/"+TABLE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/example/colors" + TABLE;

    public class Columns {
        public static final String VALUE = "value";
        public static final String SATURATION = "saturation";
        public static final String HUE = "hue";
        public static final String NAME = "name";
        public static final String _ID = BaseColumns._ID;
    }
}
