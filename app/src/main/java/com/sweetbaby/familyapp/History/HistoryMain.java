package com.sweetbaby.familyapp.History;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sweetbaby.familyapp.MainActivity;
import com.sweetbaby.familyapp.R;

public class HistoryMain extends AppCompatActivity implements PendingFlagment.OnFragmentInteractionListener,PastorderFlagment.OnFragmentInteractionListener {

    @Override
    public boolean onSupportNavigateUp() {
        //onBackPressed();
        selectOptions();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        selectOptions();
    }

    private void selectOptions() {
        Intent ints=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(ints);
        finish();
        /*final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.historyback_layout,null);
        builder.setTitle("Select");

        builder.setView(dialogView);
        builder.setButton(DialogInterface.BUTTON_NEUTRAL, "back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            onBackPressed();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent ints=new Intent(dialogView.getContext(), MainActivity.class);
                startActivity(ints);
            }
        });



        builder.show();*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_main);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            //Fragment fragment = null;
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = TabFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
