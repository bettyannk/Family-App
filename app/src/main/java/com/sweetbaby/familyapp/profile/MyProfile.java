package com.sweetbaby.familyapp.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sweetbaby.familyapp.R;
import com.sweetbaby.familyapp.family.FamilyView;

public class MyProfile extends AppCompatActivity {

    private ImageView mSelectImage;
    private EditText myName;
    private EditText myPhone;
    private Button mySaveBtn;
    private Uri mImageUri = null;
    private ProgressDialog mProgress;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        myName = (EditText)findViewById(R.id.myNameField);
        myPhone = (EditText) findViewById(R.id.myPhoneField);
        mySaveBtn = (Button) findViewById(R.id.saveProfileBtn);
        mSelectImage = (ImageView) findViewById(R.id.myImageSelect);
        mProgress = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        mySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdding();
            }
        });
    }
    private void startAdding() {
        final String name_val = myName.getText().toString().trim();
        final String phone_val = myPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(phone_val) && mImageUri != null){
            mProgress.setTitle("Saving profile...");
            mProgress.show();
            StorageReference filepath = mStorage.child("Profile_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final  DatabaseReference newPost = mDatabase.push();
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("name").setValue(name_val);
                            newPost.child("phone").setValue(phone_val);
                            newPost.child("image").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MyProfile.this, "Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MyProfile.this, FamilyView.class));
                                    }
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), " Adding Complete!! ", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    mProgress.setMessage("Uploaded" + ((int) progress) + "%...");
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Failed... ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data!= null){
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }
}
