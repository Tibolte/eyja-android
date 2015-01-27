package fr.northborders.eyja.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.northborders.eyja.model.RssFeed;

/**
 * Created by thibaultguegan on 25/01/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final int databaseVersion = 1;
    private static final String databaseName = "dbFeeds";
    public static final String TABLE_FEED = "FeedTable";

    //feed table column names
    public static final String COL_ID = "col_id";
    public static final String COL_TITLE = "col_title";
    public static final String COL_DESCRIPTION = "col_description";
    public static final String COL_IMG_LINK = "col_img_link";
    public static final String COL_PUB_DATE = "col_pub_date";
    public static final String COL_URL = "col_url";
    public static final String COL_IMAGE_ID = "col_image_id";
    public static final String COL_IMAGE_BITMAP = "col_image_bitmap";

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_FEED + "("
                + COL_ID + " INTEGER PRIMARY KEY ,"
                + COL_TITLE + " TEXT,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_IMG_LINK + " TEXT,"
                + COL_PUB_DATE + " TEXT,"
                + COL_URL + " TEXT,"
                + COL_IMAGE_ID + " TEXT,"
                + COL_IMAGE_BITMAP + " TEXT )";
        db.execSQL(CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);
        onCreate(db);
    }

    public void flush() {
        SQLiteDatabase db = DatabaseHelper.this.getWritableDatabase();
        db.delete(TABLE_FEED, null, null);
        db.close();
    }

    public void insertFeed(RssFeed rssFeed) {

        SQLiteDatabase db = DatabaseHelper.this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, rssFeed.getTitle());
        values.put(COL_DESCRIPTION, rssFeed.getDescription());
        values.put(COL_IMG_LINK, rssFeed.getImgLink());
        values.put(COL_PUB_DATE, rssFeed.getPubDate());
        values.put(COL_URL, rssFeed.getUrl().toString());

        db.insert(TABLE_FEED, null, values);

    }

}
