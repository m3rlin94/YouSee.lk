package com.uclk.shani.youseelk.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uclk.shani.youseelk.R;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    Toolbar toolbar;
    Button open;
    Button view;
    private NavigationView nview;
    private DrawerLayout drawerLayout;
    //Configuration newConfig;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedId;
    private boolean mUserSawDrawer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.app_bar_nav_drawer);
        setSupportActionBar(toolbar);

        //initializes the navigation view
        nview = (NavigationView) findViewById(R.id.navView);
        nview.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.layoutHome);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout ,toolbar,R.string.drawer_open,R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        selectedId = savedInstanceState == null ? R.id.nav_home : savedInstanceState.getInt(SELECTED_ITEM_ID);
        navigate(selectedId);





        open = (Button) findViewById(R.id.btnPlan);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getApplicationContext(),MainTourActivity.class);
                startActivity(intent1);
            }
        });

        view = (Button) findViewById(R.id.btnView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getApplicationContext(),ViewActivity.class);
                startActivity(intent2);
            }
        });

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
}
