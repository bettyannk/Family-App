package com.sweetbaby.familyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.sweetbaby.familyapp.History.HistoryMain;
import com.sweetbaby.familyapp.family.AddFamily;
import com.sweetbaby.familyapp.family.FamilyView;
import com.sweetbaby.familyapp.profile.MyProfile;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    Button viewfam;
    ImageView editProfile,prof;
    SharedPreferences pref;
    String name,phone,key,image;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        pref=getApplicationContext().getSharedPreferences("userdata",0);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        viewfam= findViewById(R.id.viewFam);

        editProfile = headerView.findViewById(R.id.editProfile);
        prof=headerView.findViewById(R.id.imageView);
        username=headerView.findViewById(R.id.textView);

        name=pref.getString("uname",null);
        if (name==null||name==""){
            startActivity(new Intent(getApplicationContext(),MyProfile.class));
        }
        else {
            image=pref.getString("uimage",null);
            Picasso.with(this)
                    .load(image)
                    .into(prof);
            username.setText(name);


        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyProfile.class);
                startActivity(intent);
                finish();
            }
        });


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        viewfam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, MapperActivity.class);
                //startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, HistoryMain.class);
                startActivity(intent);
            }
        });


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
    public void onMapReady(GoogleMap googleMap) {
        LatLng nairobi = new LatLng(-1.27, 36.92);
        float zoom = 15;
        googleMap.addMarker(new MarkerOptions().position(nairobi)
                .title("Marker in Nairobi"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nairobi,zoom));

        googleMap.addMarker(new MarkerOptions().position(new LatLng(-1.279939, 36.925286)).title("Mother"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-1.255305, 36.873873)).title("Sister"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-1.291701, 36.826280)).title("Father"));
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    //@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view) {
            // Handle
            Intent intent = new Intent(MainActivity.this, HistoryMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_messages) {


        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this, AddFamily.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_family) {
            Intent intent = new Intent(MainActivity.this, FamilyView.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.editProfile) {
            Intent intent = new Intent(MainActivity.this, MyProfile.class);
            startActivity(intent);
            finish();

            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
