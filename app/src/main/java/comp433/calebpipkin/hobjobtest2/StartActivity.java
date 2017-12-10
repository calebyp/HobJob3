package comp433.calebpipkin.hobjobtest2;

/**
 * Created by calebyp on 12/9/17.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    Button search_button;
    Button create_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        search_button = (Button) findViewById(R.id.button);
        search_button = (Button) findViewById(R.id.button);
        create_button = (Button) findViewById(R.id.button2);
    }

    public void lcreate(View v) {
        Intent createIntent = new Intent(StartActivity.this, CreateActivity.class);
        createIntent.putExtra("Uniqid", "From_Activity_B");
        startActivity(createIntent);
    }

    public void lsearch(View v) {
        Intent searchIntent = new Intent(StartActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }
}
