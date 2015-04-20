package com.example.rummezheni.price_history;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rummezheni on 17.04.2015.
 */
public class MyListActivity extends ListActivity implements  AdapterView.OnItemLongClickListener{

    ArrayList<Integer> idList = new ArrayList<Integer>();
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(this);
        bindAdapter();
    }

    public void bindAdapter()
    {
        String[] from = new String[] {MyContentProvider.ITEM_NAME, MyContentProvider.ITEM_PRICE};
        int[] to =  new int[] {android.R.id.text1, android.R.id.text2};
        //Creating cursor
        Cursor queryCursor = this.getContentResolver().query(MyContentProvider.CONTENT_URI,null,null,null,null);

        if (queryCursor != null) {
            //setting notithcation uri for created cursor
            queryCursor.setNotificationUri(this.getContentResolver(),
                    MyContentProvider.CONTENT_URI);
            mAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, queryCursor, from, to, 0);
            setListAdapter(mAdapter);
            queryCursor.moveToFirst();
            idList = new ArrayList<Integer>(queryCursor.getCount());
            do {
                if (queryCursor.getCount() > 0)
                    idList.add(queryCursor.getInt(queryCursor.getColumnIndex(MyContentProvider.ITEM_ID)));
            }
            while (queryCursor.moveToNext());
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("onList item click","on list item click");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                showAddActivity();
                return true;
            case R.id.action_settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings() {
    }

    private void showAddActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        Toast.makeText(getApplicationContext(),
                selectedItem + " deleted.",
                Toast.LENGTH_SHORT).show();
        int itemId = idList.get(position);
        getContentResolver().delete(MyContentProvider.CONTENT_URI, MyContentProvider.ITEM_ID + "=?", new String[]{"" + itemId});
        //TODO change udate with Data Set Observer
        //========
        //mAdapter.notifyDataSetChanged();
        //mAdapter.swapCursor(this.getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, null));
        //========

        return true;

    }


}
