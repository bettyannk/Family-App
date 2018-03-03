package com.sweetbaby.familyapp.family;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.sweetbaby.familyapp.R;

public class FamilyView extends AppCompatActivity {
    private RecyclerView mFamilyList;
    private DatabaseReference mDatabase;
    SharedPreferences pref;
    String key,tophone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My family");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pref = getApplicationContext().getSharedPreferences("userdata", 0);
        key = pref.getString("ukey", null);

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
        myMethod( key);

    }

    private void myMethod(String key) {
        FirebaseRecyclerAdapter<Family, FamilyViewHolder > firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Family, FamilyViewHolder>(
                        Family.class,
                        R.layout.family_row,
                        FamilyViewHolder.class,
                        mDatabase.child("Family").child(key)
                ) {
                    @Override
                    protected void populateViewHolder(FamilyViewHolder viewHolder, final Family model, int position) {
                        viewHolder.setName(model.getName());
                        viewHolder.setPhone(model.getPhone());

                        viewHolder.setRelationship(model.getRelationship());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        viewHolder.card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              //  String p=mDatabase.child("Users").child()
                                tophone=model.getPhone().toString();
                                Log.e("correctt", "is null"+tophone);
                              final Query q=mDatabase.child("Users").orderByChild("phone").equalTo(tophone);
                                if (q.equals(null)){
                                    Log.e("correct", "is null"+q.toString());
                                }else {
                                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.e("correct", q.toString() + "\t" + dataSnapshot.toString());
                                            try{
                                            if (dataSnapshot.getValue().toString() != null) {
                                               Log.e("correctkey", dataSnapshot.getChildren().toString());
                                                for (DataSnapshot mdata:dataSnapshot.getChildren()){
                                                    Log.e("correctkey",  mdata.child("ukey").getValue().toString());
                                                    String ckey=mdata.child("ukey").getValue().toString();
                                                    String name=mdata.child("name").getValue().toString();
                                                    getSupportActionBar().setTitle(name+"\'s\t family");
                                                    myMethod(ckey);
                                                    break;
                                                }
                                            }
                                            }catch (Exception e){
                                                Toast.makeText(FamilyView.this, "Not joined", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });
                    }
                };
        mFamilyList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FamilyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CardView card;
        public FamilyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            card=mView.findViewById(R.id.card);
        }
        public void setName(String name){
            TextView family_name = (TextView)mView.findViewById(R.id.family_name);
            family_name.setText(name);
        }
        public void setPhone(String phone){
            TextView family_phone = (TextView)mView.findViewById(R.id.family_phone);
            family_phone.setText(phone);
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
