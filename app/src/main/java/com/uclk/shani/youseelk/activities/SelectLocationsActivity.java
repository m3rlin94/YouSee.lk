package com.uclk.shani.youseelk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.uclk.shani.youseelk.R;
import com.uclk.shani.youseelk.adapters.PlaceCardAdapter;
import com.uclk.shani.youseelk.objects.PlaceCard;

import java.util.ArrayList;
import java.util.List;


public class SelectLocationsActivity extends SampleActivityBase implements NavigationView.OnNavigationItemSelectedListener
{
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    Button search;
    TextView selected;
    private static int count = 0;

//    private TourDetails td;

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private int viewid;
    Toolbar toolbar;

    private NavigationView nview;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedId;
    private boolean mUserSawDrawer = false;

    private String[] allLocs;
    private String[] temp;
    private String sl;

    public static List<String> placeTags = new ArrayList<String>(){
        {   add("Chill");
            add("Hills");
            add("Historical");
            add("Beach");
        }
    };

    public static List<String> addedLocations = new ArrayList<>();

    private RecyclerView rvAddedPlaces;
    private PlaceCardAdapter placeCardAdapter;
    private List<PlaceCard> placeCardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_locations);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);

        //Navigation Drawer
        //initializes the navigation view
        nview = (NavigationView) findViewById(R.id.navView);
        nview.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.layoutSelectLocations);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout ,toolbar,R.string.drawer_open,R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        selectedId = savedInstanceState == null ? R.id.layoutSelectLocations : savedInstanceState.getInt(SELECTED_ITEM_ID);
        navigate(selectedId);

        Button next1 = (Button) findViewById(R.id.btnNext1);

        //Passing intents
//        Intent bundle = getIntent();
//        td = (TourDetails) bundle.getSerializableExtra("TOUR_DETAILS");
//        if (td == null) {
//            com.uclk.shani.youseelk.logger.Log.i("location", "Null object");
//        } else {
//
//            com.uclk.shani.youseelk.logger.Log.i("location", td.getEndingLocation());
//            com.uclk.shani.youseelk.logger.Log.i("location", td.getStartingLocation());
//        }

        sl = getIntent().getStringExtra("STARTING_LOCATION");




        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                temp = addedLocations.toArray(new String[0]);
                allLocs = new String[temp.length+1];
                allLocs[0] = sl;
                System.arraycopy(temp, 0, allLocs, 1, temp.length);

                Intent intent = new Intent(getApplicationContext(),DurationActivity.class);
                intent.putExtra("ADDED_LOCATIONS",allLocs);
                startActivity(intent);
            }
        });


        search = (Button) findViewById(R.id.btnSearchPlaces);
        selected = (TextView) findViewById(R.id.tvSelectedLoc);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutoCompleteActivity();
            }
        });




        rvAddedPlaces = (RecyclerView)findViewById(R.id.rvPlaceCards);
        placeCardsList = new ArrayList<>();
        placeCardAdapter = new PlaceCardAdapter(this,placeCardsList);

        RecyclerView.LayoutManager manager = new GridLayoutManager(this,2);
        rvAddedPlaces.setLayoutManager(manager);
        rvAddedPlaces.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        rvAddedPlaces.setItemAnimator(new DefaultItemAnimator());
        rvAddedPlaces.setAdapter(placeCardAdapter);


    }

    //need to edit image sources
    private void createCard(String n, String x){

        int[] thumbnails = new int[]{
                R.drawable.alawatugoda,
                R.drawable.kandy,
                R.drawable.matale,
                R.drawable.colombo,
                R.drawable.pasikudah};

        PlaceCard obj = new PlaceCard(n,x,thumbnails[1],placeTags);
        placeCardsList.add(obj);
        placeCardAdapter.notifyDataSetChanged();
    }


    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void openAutoCompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        }
        catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        }
        catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());

                String selectedPlace = place.getName().toString();
                selected.setText(selectedPlace);

                // Format the place's details and display them in the TextView.
                if(!addedLocations.contains(selectedPlace)){
                    addedLocations.add(selectedPlace);
                    createCard(selectedPlace, place.getAddress().toString());
                    count++;
                    selected.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(),"You have already added " + selectedPlace,Toast.LENGTH_LONG).show();
                    selected.setText("");
                }



            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            }
            else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
                Toast.makeText(getApplicationContext(),"No selection was made!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void navigate(int selectedId)
    {

        Intent intent = null;
        if(selectedId == R.id.layoutSelectLocations){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if(selectedId == R.id.nav_myplans){
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this,MyPlansActivity.class);
            startActivity(intent);
        }


        if(selectedId == R.id.nav_about){
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }

        if(selectedId == R.id.nav_settings){
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        if(selectedId == R.id.nav_logout){
            drawerLayout.closeDrawer(GravityCompat.START);
            Toast.makeText(getApplicationContext(), "Logging out...",Toast.LENGTH_SHORT).show();
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuitem)
    {

        menuitem.setChecked(true);
        selectedId = menuitem.getItemId();
        navigate(selectedId);

        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID,selectedId);
    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}




