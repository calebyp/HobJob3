package comp433.calebpipkin.hobjobtest2;

/**
 * Created by calebyp on 12/9/17.
 */
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static comp433.calebpipkin.hobjobtest2.R.id.btnAdd;

public class ViewActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    SQLiteDatabase db;
    int idCount = 0;
    ArrayList<String> inputTags, nullArray;
    String str = "";

    String stringSize;
    ImageView pic;
    Button captureButton;

    EditText tags;
    EditText size;
    TextView tv;
    Spinner spintype;
    EditText contactTV;
    EditText eventTV;
    EditText descriptionTV;
    EditText imageTV;
    Button btnAdd;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        inputTags = new ArrayList<String>();
        nullArray = new ArrayList<String>();

        pic = (ImageView) findViewById(R.id.imageView);
        captureButton = (Button) findViewById(R.id.captureButton);

        db = this.openOrCreateDatabase("MyDatabase", Context.MODE_PRIVATE, null);

        db.execSQL("DROP TABLE IF EXISTS Photos");
        db.execSQL("CREATE TABLE Photos (ID INTEGER, location TEXT, size INTEGER) ");

        db.execSQL("DROP TABLE IF EXISTS Tags");
        db.execSQL("CREATE TABLE Tags (ID INTEGER, tag TEXT) ");
        spintype = (Spinner) findViewById(R.id.spinner);
        contactTV = (EditText) findViewById(R.id.contact);
        eventTV = (EditText) findViewById(R.id.event);
        descriptionTV = (EditText) findViewById(R.id.descript);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        mDatabaseHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spintype.getSelectedItem().toString();
                String contact = contactTV.getText().toString();
                String event = eventTV.getText().toString();
                String description = descriptionTV.getText().toString();
                if (type.length() <= 5 && contact.length() != 0 && event.length() != 0 && description.length() != 0) {
                    AddData(type, contact, event, description, selectedImagePath);
                } else {
                    toastMessage("You must put something in all fields!");
                }

            }
        });


    }

    public void AddData(String type, String contact, String event, String disc, String image) {
        boolean insertData = mDatabaseHelper.addData(type, contact, event, disc, image);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void goLoad(View v) {
        String q = "Select Photos.ID, location, size, Tags.tag FROM Photos, Tags WHERE Photos.ID = Tags.ID";
        double s = Double.parseDouble(size.getText() + "");
//        double low = s-s*.25;
//        double high = s+s*.25;
        //gets image by size +/- 25%
        if (size.length() > 0)
            q = q + " AND size BETWEEN '" + (s - s * .25) + "' AND '" + (s + s * .25) + "'";
        if (tags.length() > 0) {
            inputTags = nullArray;
            String tagString = "";
            tagString = tagString + tags.getText();
            tagString = tagString.replaceAll("\\s+", "");
            String temp = "";
            for (int i = 0; i < tagString.length(); i++) {
                if (tagString.charAt(i) == ';') {
                    inputTags.add(temp);
                    temp = "";
                    i++;
                }
                temp = temp + Character.toString(tagString.charAt(i));
            }
            inputTags.add(temp);
            q = q + " AND tag = '" + tags.getText() + "'";
        }

        Cursor c = db.rawQuery(q, null);
        c.moveToFirst();
        str = "";
        for (int j = 0; j < c.getCount(); j++) {
            for (int i = 0; i < c.getColumnCount(); i++) {
                str = str + c.getString(i) + " ";
            }
            str += "\n";
            tv.setText(str);
            String dir = "";
            for (int i = 2; i < str.length(); i++) {
                if (str.charAt(i) == ' ')
                    break;
                dir = dir + str.charAt(i);
            }
            loadImage(dir);
            c.moveToNext();
        }
    }

    protected void goSave(View v) {
        inputTags = nullArray;
        idCount++;
        String tagString = "";
        if (tags.length() > 0) {
            tagString = tagString + tags.getText();
            if (tagString.contains(";"))
                db.execSQL("INSERT INTO Tags VALUES ('" + idCount + "', '" + tagString + "')");
            tagString = tagString.replaceAll("\\s+", "");
            String temp = "";
            // if (tagString.contains(";")) {
            for (int i = 0; i < tagString.length(); i++) {
                if (tagString.charAt(i) == ';') {
                    inputTags.add(temp);
                    temp = "";
                    i++;
                }
                temp = temp + Character.toString(tagString.charAt(i));
            }
            inputTags.add(temp);
            BitmapDrawable drawable = (BitmapDrawable) pic.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String directory = SaveImage(bitmap);
            String q = "INSERT INTO Photos VALUES ('";
            q = q + idCount + "', '" + directory + "', '" + stringSize + "')";
            db.execSQL(q);
            for (int i = 0; i < inputTags.size(); i++) {
                q = "INSERT INTO Tags VALUES ('" + idCount + "', '" + inputTags.remove(i) + "')";
                db.execSQL(q);
            }
        }
    }

    private String SaveImage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir" + idCount, Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImage(String path) {
        try {
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            pic.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void goCamera(View v) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            pic.setImageBitmap(photo);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Log.v("tag", "gallery load");
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                pic.setImageURI(selectedImageUri);
                Log.v("tag", selectedImagePath);
//                Uri imgUri=Uri.parse(selectedImagePath);
//                pic.setImageURI(null);
//                pic.setImageURI(imgUri);
            }
        }
    }

    public void goGallery(View v) {

        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void createToSearch(View v) {
        Intent createIntent = new Intent(ViewActivity.this, SearchActivity.class);
        startActivity(createIntent);
    }

    public void createToStart(View v) {
        Intent backIntent = new Intent(ViewActivity.this, StartActivity.class);
        startActivity(backIntent);
    }
}
