package com.example.rummezheni.application1;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by rummezheni on 17.04.2015.
 */
public class MyListActivity extends ListActivity{

    ArrayList<Integer> idList = new ArrayList<Integer>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Creating cursor
        Cursor queryCursor = this.getContentResolver().query(MyContentProvider.CONTENT_URI,null,null,null,null);
        startManagingCursor(queryCursor);

        String[] from = new String[] {MyContentProvider.ITEM_NAME, MyContentProvider.ITEM_PRICE};
        int[] to =  new int[] {android.R.id.text1, android.R.id.text2};
        ListAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,queryCursor,from,to,0);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new myOnClickListener(getListView()));

        queryCursor.moveToFirst();
        idList = new ArrayList<Integer>(queryCursor.getCount());
        do {
            idList.add(queryCursor.getInt(queryCursor.getColumnIndex(MyContentProvider.ITEM_ID)));
        }
        while (queryCursor.moveToNext());
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

    private void showList()
    {



    }

    private void showSettings() {
    }

    private void showAddActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }


    private class myOnClickListener implements AdapterView.OnItemClickListener {

        ListView listView;

        public myOnClickListener(ListView v)
        {
            listView = v;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int itemId = idList.get(position);
            getContentResolver().delete(MyContentProvider.CONTENT_URI,MyContentProvider.ITEM_ID+"=?",new String[]{""+itemId});
            //listView.getAdapter().noti
            Log.d("LOG_TAG", "id for " + position + " is " + itemId);
        }
    }

    


}
