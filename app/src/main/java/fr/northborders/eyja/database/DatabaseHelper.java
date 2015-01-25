package fr.northborders.eyja.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import fr.northborders.eyja.model.RssFeed;
import fr.northborders.eyja.util.Utils;

/**
 * Created by thibaultguegan on 25/01/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final int databaseVersion = 1;
    private static final String databaseName = "dbFeeds";
    private static final String TABLE_FEED = "FeedTable";

    private static final String IMAGE_ID_PREFIX = "image_";

    //feed table column names
    private static final String COL_ID = "col_id";
    private static final String COL_TITLE = "col_title";
    private static final String COL_DESCRIPTION = "col_description";
    private static final String COL_IMG_LINK = "col_img_link";
    private static final String COL_PUB_DATE = "col_pub_date";
    private static final String COL_URL = "col_url";
    private static final String COL_IMAGE_ID = "col_image_id";
    private static final String COL_IMAGE_BITMAP = "col_image_bitmap";

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
        InsertFeedTask insertFeedTask = new InsertFeedTask(rssFeed);
        insertFeedTask.execute();
    }

    private class InsertFeedTask extends AsyncTask<Void, Void, Void> {

        private RssFeed rssFeed;

        public InsertFeedTask(RssFeed rssFeed) {
            this.rssFeed = rssFeed;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase db = DatabaseHelper.this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_TITLE, rssFeed.getTitle());
            values.put(COL_DESCRIPTION, rssFeed.getDescription());
            values.put(COL_IMG_LINK, rssFeed.getImgLink());
            values.put(COL_PUB_DATE, rssFeed.getPubDate());
            values.put(COL_URL, rssFeed.getUrl().toString());

            //image
/*            String imageId = IMAGE_ID_PREFIX + Utils.generateViewId();
            values.put(COL_IMAGE_ID, imageId);
            try {
                Drawable drawable = Utils.drawableFromUrl(rssFeed.getImgLink());
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                values.put(COL_IMAGE_BITMAP, stream.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                db.insert(TABLE_FEED, null, values);
                db.close();
            }*/

            return null;
        }
    }

    public ImageHelper getFeedImage(RssFeed rssFeed) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2 = db.query(TABLE_FEED,
                new String[] {COL_ID, COL_IMAGE_ID, COL_IMAGE_BITMAP},COL_URL
                        +" LIKE '"+rssFeed.getUrl().toString()+"%'", null, null, null, null);
        ImageHelper imageHelper = new ImageHelper();

        if (cursor2.moveToFirst()) {
            do {
                imageHelper.setImageId(cursor2.getString(1));
                imageHelper.setImageByteArray(cursor2.getBlob(2));
            } while (cursor2.moveToNext());
        }

        cursor2.close();
        db.close();
        return imageHelper;

    }
}
