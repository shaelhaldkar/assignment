package com.my.assignment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private Spinner sortSpinner;
    private Spinner rowNumberSpinner;

    GridView gridView[];

    final String sortType[] = {"Artist", "Album"};
    final String rowNumber[] = {"1", "2", "3", "4", "5"};
    int maxValue = 37;
    String songName[] = new String[maxValue];
    String artistName[] = new String[maxValue];
    String albumName[] = new String[maxValue];
    String uniqueArtistName[] = new String[maxValue];
    String uniqueAlbumName[] = new String[maxValue];
    ArrayList<String> songAlbumList[] = new ArrayList[maxValue];
    ArrayList<String> songArtistList[] = new ArrayList[maxValue];
    int uniqueAlbumCounter = 0;
    int uniqueArtistCounter = 0;
    int rowSelected = 1;
    String selectedSortType = "";
    LinearLayout linearLayout;
    int songSize;
    int columnWidth = 430;
    int horizontalSpacing = 40;
    int verticalSpacing = 40;
    float screenDensity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;


        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, sortType);
        ArrayAdapter<String> rowNumberAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, rowNumber);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.custom_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        rowNumberSpinner = (Spinner) findViewById(R.id.rowNumberSpinner);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        sortSpinner.setAdapter(sortAdapter);
        rowNumberSpinner.setAdapter(rowNumberAdapter);
        rowNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long l) {
                rowSelected = Integer.parseInt(rowNumber[itemPosition]);
                updateGridLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long l) {
                selectedSortType = sortType[itemPosition];
                if (itemPosition == 0) {
                    uniqueArtistCounter = 0;
                    for (int i = 0; i < maxValue; i++) {
                        int artistPosition = findArtist(artistName[i], uniqueArtistCounter);
                        if (artistPosition == -1) {
                            uniqueArtistName[uniqueArtistCounter] = artistName[i];
                            songArtistList[uniqueArtistCounter] = new ArrayList<String>();
                            songArtistList[uniqueArtistCounter].add(songName[i]);
                            uniqueArtistCounter++;
                        } else {
                            songArtistList[artistPosition].add(songName[i]);
                        }
                    }
                } else //album sort selected
                {
                    uniqueAlbumCounter = 0;
                    for (int i = 0; i < maxValue; i++) {
                        int albumPosition = findAlbum(albumName[i], uniqueAlbumCounter);
                        if (albumPosition == -1) {
                            uniqueAlbumName[uniqueAlbumCounter] = albumName[i];
                            songAlbumList[uniqueAlbumCounter] = new ArrayList<String>();
                            songAlbumList[uniqueAlbumCounter].add(songName[i]);
                            uniqueAlbumCounter++;
                        } else {
                            songAlbumList[albumPosition].add(songName[i]);
                        }
                    }

                }
                setUpGrid();

                updateGridLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        BufferedReader bufferedReader = null;
        String lineString = "";
        try {
            /** pass is used for while loop **/
            int pass = 0;

            /* read sample_music_data.csv file */
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("sample_music_data.csv")));

            /* read first line */
            lineString = bufferedReader.readLine();

            /* starting from second line read line by line till line value is not null */
            while ((lineString = bufferedReader.readLine()) != null) {
                /** split the line with comma (,) **/
                String tempString[] = lineString.split(",");

                /** store song name in songName array **/
                songName[pass] = tempString[0];

                /** store artist name in artistName array **/
                artistName[pass] = tempString[1];

                /** store album name in albumName array **/
                albumName[pass] = tempString[2];

                /** increment pass **/
                pass++;

            }
        } catch (Exception e) {
            Log.d("Collection", "Failed");
        }

    }

    private void setUpGrid() {
        /** clear all views in linear layout */
        linearLayout.removeAllViews();

        /** Maximum number of GridView to be displayed */
        int maxGridCount = 0;

        /** selected unique artist value if artist is selected or select unique album count*/
        if (selectedSortType.equals("Artist")) {
            maxGridCount = uniqueArtistCounter;
        } else {
            maxGridCount = uniqueAlbumCounter;
        }

        /** create array of GridView to be displayed **/
        gridView = new GridView[maxGridCount];

        /** for each artist or album name */
        for (int i = 0; i < maxGridCount; i++) {

            /** Set up layout parameters width and height */
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            /** create new textview*/
            TextView textView = new TextView(this);

            /** set textview text */
            if (selectedSortType.equals("Artist")) {
                textView.setText(uniqueArtistName[i]);
            } else {
                textView.setText(uniqueAlbumName[i]);
            }

            /** set textview layout */
            textView.setLayoutParams(layoutParams);

            /* calculate padding space*/
            int paddingSpace = (int) (30 / screenDensity);


            textView.setPadding(paddingSpace, paddingSpace, paddingSpace, paddingSpace);


            linearLayout.addView(textView);


            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);


            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            horizontalScrollView.setLayoutParams(layoutParams);


            LinearLayout innerLinearLayout = new LinearLayout(this);
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            innerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            innerLinearLayout.setLayoutParams(layoutParams);


            CustomGrid customGrid;


            if (selectedSortType.equals("Artist")) {
                customGrid = new CustomGrid(context, songArtistList[i]);
            } else {
                customGrid = new CustomGrid(context, songAlbumList[i]);
            }


            gridView[i] = new GridView(this);


            gridView[i].setColumnWidth((int) (columnWidth / screenDensity));


            gridView[i].setNumColumns(2);
            gridView[i].setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            gridView[i].setVerticalSpacing((int) (verticalSpacing / screenDensity));
            gridView[i].setHorizontalSpacing((int) (horizontalSpacing / screenDensity));


            paddingSpace = (int) (20 / screenDensity);
            gridView[i].setPadding(paddingSpace, paddingSpace, paddingSpace, paddingSpace);


            if (selectedSortType.equals("Artist")) {
                layoutParams = new ViewGroup.LayoutParams(columnWidth * songArtistList[i].size(), 200);
            } else {
                layoutParams = new ViewGroup.LayoutParams(columnWidth * songAlbumList[i].size(), 200);
            }


            gridView[i].setLayoutParams(layoutParams);


            gridView[i].setAdapter(customGrid);


            innerLinearLayout.addView(gridView[i]);


            horizontalScrollView.addView(innerLinearLayout);


            linearLayout.addView(horizontalScrollView);
        }
    }


    private void updateGridLayout() {

        if (selectedSortType.equals(sortType[0])) {

            for (int i = 0; i < uniqueArtistCounter; i++) {

                songSize = songArtistList[i].size();


                int numCol = songSize / rowSelected;


                if (songSize % rowSelected != 0)
                    numCol++;


                gridView[i].setNumColumns(numCol);


                int numRows = songSize / numCol;
                if (songSize % numCol != 0)
                    numRows++;


                LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(columnWidth * numCol, 150 * numRows);


                gridView[i].setLayoutParams(newLayoutParams);

            }

        } else {

            for (int i = 0; i < uniqueAlbumCounter; i++) {

                songSize = songAlbumList[i].size();


                int numCol = songSize / rowSelected;


                if (songSize % rowSelected != 0)
                    numCol++;
                gridView[i].setNumColumns(numCol);


                int numRows = songSize / numCol;
                if (songSize % numCol != 0)
                    numRows++;


                LinearLayout.LayoutParams newLayoutParams = new LinearLayout.LayoutParams(columnWidth * numCol, 100 * numRows);


                gridView[i].setLayoutParams(newLayoutParams);

            }

        }

    }


    private int findAlbum(String nameToSearch, int position) {
        int foundIndex = -1;
        for (int i = 0; i < position; i++) {
            if (uniqueAlbumName[i].equals(nameToSearch)) {
                foundIndex = i;
                return i;
            }
        }
        return foundIndex;

    }


    private int findArtist(String nameToSearch, int position) {
        int foundIndex = -1;
        for (int i = 0; i < position; i++) {
            if (uniqueArtistName[i].equals(nameToSearch)) {
                foundIndex = i;
                return i;
            }
        }
        return foundIndex;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_song_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}