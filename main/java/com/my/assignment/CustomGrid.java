package com.my.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter {

    /**
     * reference of Context class
     */
    private Context context;

    /**
     * arrarList of songName to be populated
     */
    ArrayList<String> songNameList = new ArrayList();

    CustomGrid(Context context, ArrayList<String> songNameList) {
        this.context = context;
        this.songNameList = songNameList;
    }

    /**
     * return length of arraylist
     */
    @Override
    public int getCount() {
        return songNameList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        /** reference of View */
        View grid;

        /** instance of LayoutInflater to be inflated */
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /** if counterView is not initialized */
        if (convertView == null) {
            /** initialize grid View */
            grid = new View(context);

            /** set layoutInflator to grid */
            grid = layoutInflater.inflate(R.layout.single_song, null);

            /** instance of textView from songLabel file(XML) */
            TextView songNameTextView = (TextView) grid.findViewById(R.id.songLabel);

            /** set text to textView */
            songNameTextView.setText(songNameList.get(position));

        } else {
            /** assign grid to convertView since its initialized */
            grid = (View) convertView;
        }

        /** return grid */
        return grid;
    }
}
