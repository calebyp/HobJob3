package comp433.calebpipkin.hobjobtest2;

/**
 * Created by calebyp on 12/9/17.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    SQLiteDatabase sqlitedatabase;

    DataProvider dataprovider;
    Cursor mCursor;
    MyListAdapter mylistadapter;

    String type, contact, event, description, image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        sqlitedatabase = mDatabaseHelper.getReadableDatabase();
        //Comment this to use single list
        mylistadapter = new MyListAdapter(getApplicationContext(), R.layout.single_post);
        mListView.setAdapter(mylistadapter);
        mCursor = mDatabaseHelper.getData();

        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String> listCollection = new ArrayList<>();

        while (data.moveToNext()) {
            listData.add(data.getString(1));
            type = data.getString(1);
            contact = data.getString(2);
            event = data.getString(3);
            description = data.getString(4);
            image = data.getString(5);
            //listData.add(data.getString(5));

            DataProvider dataProvider = new DataProvider(type, contact, event, description, image);

            listCollection.add(data.getString(1));
        }

        //Comment this out for the simple list, clicking works
        if(mCursor.moveToFirst()){
            do{
                String type, contact, event, description, image;
                type = mCursor.getString(1);
                contact = mCursor.getString(2);
                event = mCursor.getString(3);
                description = mCursor.getString(4);
                image = mCursor.getString(5);

                DataProvider dataProvider = new DataProvider(type, contact, event, description, image);
                mylistadapter.add(dataProvider);

            }
            while (mCursor.moveToNext());
        }

        //create the list adapter and set the adapter
//        MyListAdapter listAdapter = new MyListAdapter(getApplicationContext(), R.layout.single_post, listData);
//        mListView.setAdapter(listAdapter);


//        Uncomment this to use singlelist
//        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
//        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);


                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
                int itemID = 0;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }
                Cursor data1 = mDatabaseHelper.getData(name);
                while ((data1.moveToNext())){
                    type = data1.getString(1);
                    contact = data1.getString(2);
                    event = data1.getString(3);
//                    description = data.getString(4);
                }

                if (itemID > -1) {
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(SearchActivity.this, CreateActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("name", name);
                    editScreenIntent.putExtra("Uniqid","From_Activity_A");
                    editScreenIntent.putExtra("contact", contact);
                    editScreenIntent.putExtra("type", type);
                    editScreenIntent.putExtra("event", event);
                    editScreenIntent.putExtra("description", description);
                    startActivity(editScreenIntent);
                } else {
                    toastMessage("No ID associated with that name");
                }
            }
        });

    }

    /**
     * customizable toast
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void searchToCreate(View v) {
        Intent createIntent = new Intent(SearchActivity.this, CreateActivity.class);
        startActivity(createIntent);
    }

    public void searchToStart(View v) {
        Intent backIntent = new Intent(SearchActivity.this, StartActivity.class);
        startActivity(backIntent);
    }
}