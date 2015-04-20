package com.example.rummezheni.price_history;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by rummezheni on 15.04.2015.
 */
public class MyContentProvider extends ContentProvider{

    public static final Uri CONTENT_URI = Uri.parse("content://com.training.contentprovidersample/items");
    public static final String ITEM_ID = "_id";
    public static final String TABLE_NAME = "items";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_NAME = "name";

    SQLiteDatabase db;

    /** Reference to SQLiteOpenHelper.
     * We will use our own private Database Helper class
     * to track the db access and updates (see this class definition below)
     */
    private DatabaseHelper mDatabaseHelper;

    /**
     * this is the name of our db file that we created on previous step
     */
    private static final String DATABASE_NAME = "items.db";

    /** Database version. */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constant mapped to uri "com.training.contentprovidersample/books"} via mUriMatcher.
     * We will use this constant in switch to decide what query we should perform depends on requested URI. You will see it later.
     */
    private static final int ITEMS = 1;
    /**
     * Constant mapped to uri "com.training.contentprovidersample/books/#" via mUriMatcher.
     * We will use this constant in switch to decide what query we should perform depends on requested URI. You will see it later.
     */
    private static final int ITEMS_ID = 2;

    /** Local UriMatcher.
     * this class will help us parse the passed URI to content provider
     */
    private static final UriMatcher mUriMatcher;

    /** Now we initialize UriMatcher and add two URI that we support in our content provider
     * first parameter is authority, second parameter is path and the last parameter is code that will be assigned for the URI if it matches the path
     * We support two URI: first one is to get all books, and the second one to get particular book by id
     */
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI("com.training.contentprovidersample", "items", ITEMS);
        mUriMatcher.addURI("com.training.contentprovidersample", "items/#", ITEMS_ID);
    }


    /** Utility class for db creation and update. */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * Default constructor.
         * @param context - parent context.
         * We invoke super class constructor here and pass database name and version
         * Third parameter is CursorFactory, we don't need to customize it, so it's null
         */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Because we already created table manually in our db on previous steps,
         * we don't want to recreate it again.
         * But in real application you might want to create table in a code.
         * Here is how you can do it
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " +TABLE_NAME
                    + "( "+ ITEM_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + " "+ ITEM_NAME +" VARCHAR(100), "
                    + " "+ITEM_PRICE+" VARCHAR(50));");
        }

        /**
         * In case your db version is updated, you might want to make some update in db structure.
         * In this simple app we don't want to do any changes.
         * Still the commented code has some stupid implementation: drop all tables and recreate them.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // drop db
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            // create new db
            onCreate(db);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        db = mDatabaseHelper.getWritableDatabase();
        String tableName = uri.getLastPathSegment();
        long rowID = db.delete(tableName,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        //Log.d(LOG_TAG, "insert, " + uri.toString());
        db = mDatabaseHelper.getWritableDatabase();
        long rowID = db.insert(TABLE_NAME, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return resultUri;
    }

    /**
     * In our sample application we will handle two types of URI requests:
     * 1: "com.training.contentprovidersample/items" ï¿½ to return whole list of books
     * 2: "com.training.contentprovidersample/items/#ï¿½ ï¿½ to return specific book details that matches the book id
     * (for instance com.training.contentprovidersample/books/2 to get book with id equals 2)
     * uri parameter - is URI itself that we will parse using our uri matcher
     * projection - is the list of columns that expected to be in result cursor
     * selection - is selection criteria for query
     * selectionArgs - the list of values that used in selection criteria
     * sortOrder - sorting order that client want to get for result data
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /*
         * Choose the table to query and a sort order based on the code returned for the incoming URI.
         * We use our uri matcher here
         */
        switch (mUriMatcher.match(uri)) {
            // If the incoming URI was for all of table3
            case ITEMS:
                // let's sort books by title by default
                if (TextUtils.isEmpty(sortOrder)) sortOrder = ITEM_NAME + " ASC";
                break;

            // If the incoming URI was for a single row
            case ITEMS_ID:
                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query
                 */
                selection = selection + ITEM_ID +" = " + uri.getLastPathSegment();
                break;

            default:
                // If the URI is not recognized, you should do some error handling here.
        }
        // the code to actually do the query
        // initialize the query builder
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        // get access to database via our helper
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        // set the appropriate table we want to select data from (we have only one table - books)
        qb.setTables(TABLE_NAME);
        // now execute query and get result cursor
        return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * This method is initial point for Content Provider.
     * It's invoked even before any client tries get access to provider.
     * This is good place to do some initialization.
     */
    @Override
    public boolean onCreate() {
        /**
         * All we want is create our internal Database Helper here
         */
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

}
