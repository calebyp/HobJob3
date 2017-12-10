package comp433.calebpipkin.hobjobtest2;

/**
 * Created by calebyp on 12/9/17.
 */

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends ArrayAdapter {

    List list = new ArrayList();
    DatabaseHelper databaseHelper;

    public MyListAdapter(Context context, int resource) {
        super(context, resource);
        this.list = list;
    }

    static class LayoutHandler {
        TextView type, event, description;
        ImageView image;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_post, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.type = (TextView) row.findViewById(R.id.TOR);
            layoutHandler.event = (TextView) row.findViewById(R.id.TOE);
            layoutHandler.description = (TextView) row.findViewById(R.id.description);
            layoutHandler.image = (ImageView) row.findViewById(R.id.list_image);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();
        }
        DataProvider dataProvider = (DataProvider) this.getItem(position);
        layoutHandler.type.setText(dataProvider.getType());
        layoutHandler.event.setText(dataProvider.getEvent());
        layoutHandler.description.setText(dataProvider.getDescription());

//        String imagefile = dataProvider.getImageresource();
//        if(!imagefile.isEmpty()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imagefile);
//            layoutHandler.image.setImageBitmap(myBitmap);
//        }
        //Bitmap b = BitmapFactory.decodeStream(new FileInputStream(dataProvider.getImageresource()));
        //layoutHandler.image.setImageBitmap(b);

        return row;
    }
}
