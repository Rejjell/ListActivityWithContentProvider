package com.example.rummezheni.application1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddActivity extends Activity {

    //public final static String EXTRA_MESSAGE = "com.example.rummezheni.application1.MESSAGE";

    EditText editTextName;
    EditText editTextPrice;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = (EditText) findViewById(R.id.editText);
        editTextPrice = (EditText) findViewById(R.id.editText3);
        button = (Button) findViewById(R.id.button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doInsert(View view) {


        ContentValues contentValues = new ContentValues();
        contentValues.put(MyContentProvider.ITEM_NAME, editTextName.getText().toString());
        contentValues.put(MyContentProvider.ITEM_PRICE, editTextPrice.getText().toString());
        getContentResolver().insert(MyContentProvider.CONTENT_URI,contentValues);
    }

    public void showList(View view) {
        Log.d("Tag", "ShowList called");
        Intent intent = new Intent(this, MyListActivity.class);
        startActivity(intent);
    }

}
