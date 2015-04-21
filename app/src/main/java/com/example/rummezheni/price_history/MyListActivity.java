package com.example.rummezheni.price_history;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MyListActivity extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> ,AdapterView.OnItemLongClickListener{

    // This is the Adapter being used to display the list's data.
    SimpleCursorAdapter mAdapter;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;

    static String TAG = "TAG";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Give some text to display if there is no data.  In a real
        // application this would come from a resource.
        //setEmptyText("No phone numbers");

        // We have a menu item to show in action bar.
        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                new String[] {MyContentProvider.ITEM_NAME, MyContentProvider.ITEM_PRICE },
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);



        setListAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(this);
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Place an action bar item for searching.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        // Insert desired behavior here.
        Log.i(TAG, "Item clicked: " + id);
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] ITEMS_SUMMARY_PROJECTION = new String[] {
            MyContentProvider.ITEM_ID,
            MyContentProvider.ITEM_NAME,
            MyContentProvider.ITEM_PRICE,
    };
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG,"onLoadFinished fired");
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        return new CursorLoader(this, MyContentProvider.CONTENT_URI,
                ITEMS_SUMMARY_PROJECTION, null, null,
                null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        Log.d(TAG,"onLoadFinished fired");
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        Log.d(TAG,"onLoaderReset fired");
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("DialogMessage")
                        .setTitle("DialogTitle");

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();



        /*getContentResolver()
                .delete(MyContentProvider.CONTENT_URI, MyContentProvider.ITEM_ID + "=?", new String[]{"" + id});
        getLoaderManager().restartLoader(0,null,this);*/
        Log.d(TAG,"onItemLongClick fired");
        return true;
    }
}