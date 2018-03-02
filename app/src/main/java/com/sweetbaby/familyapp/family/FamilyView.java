package com.sweetbaby.familyapp.family;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sweetbaby.familyapp.R;

public class FamilyView extends AppCompatActivity {
    private RecyclerView mFamilyList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFamilyList = (RecyclerView)findViewById(R.id.family_list);
        mFamilyList.setHasFixedSize(true);
        mFamilyList.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();  */
                startActivity(new Intent(FamilyView.this, AddFamily.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Family, FamilyViewHolder > firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Family, FamilyViewHolder>(
                        Family.class,
                        R.layout.family_row,
                        FamilyViewHolder.class,
                        mDatabase.child("Family")
                ) {
                    @Override
                    protected void populateViewHolder(FamilyViewHolder viewHolder, Family model, int position) {
                        viewHolder.setName(model.getName());
                        viewHolder.setPhonenumber(model.getPhoneNumber());
                        viewHolder.setRelationship(model.getRelationship());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                    }
                };
        mFamilyList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class FamilyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FamilyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView family_name = (TextView)mView.findViewById(R.id.family_name);
            family_name.setText(name);
        }
        public void setPhonenumber(String phonenumber){
            TextView family_phone = (TextView)mView.findViewById(R.id.family_phone);
            family_phone.setText(phonenumber);
        }
        public void setRelationship(String relationship){
            TextView family_relation = (TextView)mView.findViewById(R.id.family_relation);
            family_relation.setText(relationship);
        }
        public void setImage(final Context ctx, final String image){
            final ImageView contact_image = (ImageView)mView.findViewById(R.id.contact_image);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(contact_image, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(contact_image);
                }
            });
        }
    }
}
