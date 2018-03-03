package com.sweetbaby.familyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.sweetbaby.familyapp.History.HistoryMain;
import com.sweetbaby.familyapp.family.AddFamily;
import com.sweetbaby.familyapp.family.FamilyView;
import com.sweetbaby.familyapp.profile.MyProfile;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,  View.OnClickListener, GoogleMap.OnMarkerClickListener  {

    Button viewfam;
    ImageView editProfile,prof;
    SharedPreferences pref;
    String name,phone,key,image;
    TextView username;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    MapView mapView;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref=getApplicationContext().getSharedPreferences("userdata",0);
        setUpGClient();

        mGoogleApiClient = new GoogleApiClient
                .Builder(getApplicationContext())
                //.enableAutoManage(getActivity(), 34992, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationChecker(mGoogleApiClient, this);
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds



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


        /*SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        mapView = findViewById(R.id.map);
        mapView.getMapAsync(new OnMapReadyCallback() {
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

            }
        });
        mapView.onCreate(savedInstanceState);

        mapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                Intent intent = new Intent(MainActivity.this, FamilyView.class);
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
                .title("My last Logged Possition"));
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.e("mlastb", "" + LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient));

        Location mLastKnownLocation;
        if (location != null) {
            //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            handleNewLocation(mLastKnownLocation);
            location.getLatitude();
            location.getLongitude();
            LatLng near = new LatLng(location.getLatitude(), location.getLongitude());

            double currentLatitude = mLastKnownLocation.getLatitude();
            double currentLongitude = mLastKnownLocation.getLongitude();
            //  if (latsLongs==null)
            //  {
            String latsLongs = (currentLatitude + "," + currentLongitude);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("location",latsLongs);
            editor.apply();

        }
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            // handleNewLocation(mLastKnownLocation);
            //  Toast.makeText(this, "mnewlocationis" + LocationServices.FusedLocationApi
            //  .getLastLocation(mGoogleApiClient), Toast.LENGTH_LONG).show();
            Log.e("mlast", "" + LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient));
        } else {
            handleNewLocation(location);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Location mylocation = location;
        if (mylocation != null) {
            Double latitude=mylocation.getLatitude();
            Double longitude=mylocation.getLongitude();
            Log.e("locationx",mylocation+"");
            String yourlocation=(latitude.toString()+","+longitude.toString());
            //locationStatus.setText("Your location is set");
            //    Toast.makeText(this, "MyLoation is"+mylocation, Toast.LENGTH_SHORT).show();
            //Or Do whatever you want with your location
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public void locationChecker(final GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        //  Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        //  handleNewLocation(location);


                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    private void handleNewLocation(Location location) {
        Log.d("Locationi", location.toString());
        // Toast.makeText(getApplicationContext(), "this are"+location, Toast.LENGTH_SHORT).show();
        Log.d("newlocx2", "" + location);
        // getAdverts();
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        //  if (latsLongs==null)
        //  {
        String latsLongs = (currentLatitude + "," + currentLongitude);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("location", latsLongs);
        editor.apply();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);


        // }
        // SharedPreferences.Editor editor = pref.edit();
        editor.putString("lats", currentLatitude + "");
        editor.putString("longs", currentLongitude + "");
        editor.commit();
        // Toast.makeText(getContext(), "latslots"+currentLatitude+"\n"+currentLongitude, Toast.LENGTH_SHORT).show();

        //  googleMap.addMarker(new MarkerOptions().position(latLng).title("Your current location").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current)));

      /*  Circle circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(200)
                .fillColor(0x5500ff00)
                .strokeWidth(3).strokeColor(Color.BLACK));
        circle.setCenter(latLng);*/

        //   mapView.getMapAsync(this);
        //mLastKnownLocation = location;
        // mMap.addCircle(circle)


    }
    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
}
