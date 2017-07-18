package com.uclk.shani.youseelk.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.uclk.shani.youseelk.R;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainTourActivity extends SampleActivityBase implements NavigationView.OnNavigationItemSelectedListener
{

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private int viewid;
    Toolbar toolbar;

    private NavigationView nview;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedId;
    private boolean mUserSawDrawer = false;

    private CheckBox ret;
    Button view,next2, to, from, at, start, end;
    private String startLocation;
    private String endLocation;

    private int yearX,monthX,dayX, yearY, monthY, dayY, hourX,minuteX;
    static final int DIALOG_ID_FROM = 0;
    static final int DIALOG_ID_TO = 1;
    static final int DIALOG_ID_AT = 2;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private TextView startingPlace;
    private TextView endingPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tour);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);

        //Navigation Drawer
        //initializes the navigation view
        nview = (NavigationView) findViewById(R.id.navView);
        nview.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.layoutMainTour);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout ,toolbar,R.string.drawer_open,R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        selectedId = savedInstanceState == null ? R.id.layoutMainTour : savedInstanceState.getInt(SELECTED_ITEM_ID);
        navigate(selectedId);


        //Next and Previous Buttons

        next2 = (Button) findViewById(R.id.btnNext2);
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sl = startingPlace.getText().toString();
                Intent i = new Intent(getApplicationContext(),SelectLocationsActivity.class);
                i.putExtra("STARTING_LOCATION",sl);
                startActivity(i);

            }
        });

        //Place pickers
        start = (Button) findViewById(R.id.btnSelectStartLoc);
        end = (Button) findViewById(R.id.btnSelectEndLoc);
        startingPlace = (TextView) findViewById(R.id.tvDisplayStartLoc);
        endingPlace = (TextView) findViewById(R.id.tvDisplayEndLoc);
        ret = (CheckBox) findViewById(R.id.cbReturn);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutoCompleteActivity();
                viewid = v.getId();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutoCompleteActivity();
                viewid = v.getId();
            }
        });


        //DatePicker
        to = (Button)findViewById(R.id.btnTo);
        from = (Button) findViewById(R.id.btnFrom);

        setCurrentDates();
        showDialogOnButtonClick();

        //TimePicker
        at = (Button)findViewById(R.id.btnAtTime);

        at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_AT);
            }
        });

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

                // Format the place's details and display them in the TextView.

                if(compareIds()){
                    if(ret.isChecked()){
                        startingPlace.setText(place.getName());
                        endingPlace.setText(place.getName());
                        end.setEnabled(false);
                    }
                    else {
                        startingPlace.setText(place.getName());
                    }
                }
                else {
                    endingPlace.setText(place.getName());
                }

                ret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            endingPlace.setText(startingPlace.getText());
                            end.setEnabled(false);
                        }
                        else {
                            end.setEnabled(true);
                        }
                    }
                });

                startLocation = startingPlace.getText().toString();
                endLocation = endingPlace.getText().toString();

            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            }
            else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    private boolean compareIds() {

        if (viewid == R.id.btnSelectStartLoc){
            return true;
        } else{
            return false;
        }

    }

    /*private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }*/


    public void showDialogOnButtonClick(){

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_TO);
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_FROM);
            }
        });
    }


       @Override
    protected Dialog onCreateDialog(int id){

        switch (id){

            case DIALOG_ID_FROM:
                return new DatePickerDialog(this, fromListener, yearY, monthY, dayY);

            case DIALOG_ID_TO:
                return new DatePickerDialog(this, toListener, yearX, monthX, dayX);

            case DIALOG_ID_AT:
                return new TimePickerDialog(this,atListener, hourX,minuteX,false);
        }

        return null;
    }

    private TimePickerDialog.OnTimeSetListener atListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourX = hourOfDay;
            minuteX = minute;

            String term;
            int tempHour;

            if(hourX > 12){
                term = "p.m.";
                tempHour = hourX - 12;
            }
            else{
                term = "a.m.";
                tempHour = hourX;
            }

            TextView timeAt = (TextView)findViewById(R.id.tvDisplayAt);
            timeAt.setText(tempHour + "." + minuteX + " " + term);
        }
    };




    private DatePickerDialog.OnDateSetListener toListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            yearX = year;
            monthX = monthOfYear + 1;
            dayX = dayOfMonth;
            //Toast.makeText(MainTourActivity.this,yearX + "/" + monthX + "/" + dayX, Toast.LENGTH_LONG).show();

            TextView dto = (TextView)findViewById(R.id.tvDisplayTo);
            dto.setText(dayX + " / " + monthX + " / " + yearX);
        }
    };

    private DatePickerDialog.OnDateSetListener fromListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            yearY = year;
            monthY = monthOfYear + 1;
            dayY = dayOfMonth;
            //Toast.makeText(MainTourActivity.this,yearY + "/" + monthY + "/" + dayY, Toast.LENGTH_LONG).show();

            TextView dfrom = (TextView)findViewById(R.id.tvDisplayFrom);
            dfrom.setText(dayY + " / " + monthY + " / " + yearY);
        }
    };


    private void setCurrentDates(){

        final Calendar cal = Calendar.getInstance();
        yearX = cal.get(Calendar.YEAR);
        monthX = cal.get(Calendar.MONTH);
        dayX = cal.get(Calendar.DAY_OF_MONTH);

        yearY = cal.get(Calendar.YEAR);
        monthY = cal.get(Calendar.MONTH);
        dayY = cal.get(Calendar.DAY_OF_MONTH);

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
        if(selectedId == R.id.layoutMainTour){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if(selectedId == R.id.nav_myplans){
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this,MyPlansActivity.class);
            startActivity(intent);
        }

        /*if(selectedId == R.id.nav_upload){
            drawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this,UploadActivity.class);
            startActivity(intent);
        }*/

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


//    public void sendDataButton(View view) {
//
//        TourDetails td = new TourDetails(monthX,dayX,monthY,dayY,hourX,minuteX,startLocation,endLocation);
//        //GregorianCalendar gCal = new GregorianCalendar(//enter params)
//        Intent intent = new Intent(getApplicationContext(),SelectLocationsActivity.class);
//        intent.putExtra("TOUR_DETAILS",td);
//        startActivity(intent);
//
//
//    }
}
