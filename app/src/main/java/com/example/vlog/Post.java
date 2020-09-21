package com.example.vlog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.bind.JsonTreeReader;
import com.squareup.picasso.Picasso;

import java.io.PipedInputStream;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class Post extends AppCompatActivity {

    Uri ImageURI,setUri=null;
    ImageView imageView;
    EditText Title,Content;
    Button PostF;
    DatabaseReference databaseReference;
    private ProgressDialog pd;
    Uri MainUri=null;

    private  static final  int CAMERA_REQUEST_CODE = 100;
    private  static final  int STORAGE_REQUEST_CODE = 200;
    private  static final  int IMAGE_PICK_GALLERY_CODE = 300;
    private  static final  int IMAGE_PICK_CAMERA_CODE = 400;
    String Fname="";


    String storagePerission[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pd=new ProgressDialog(this);
        Fname = getIntent().getStringExtra("name");

        storagePerission =new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        setContentView(R.layout.activity_post);
        imageView=findViewById(R.id.ImageP);
        Title=findViewById(R.id.titleP);
        Content = findViewById(R.id.bodyP);
        PostF=findViewById(R.id.postp);

        databaseReference= FirebaseDatabase.getInstance().getReference("Posts");

        PostF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String body=Content.getText().toString();
                body=body.trim();
                body=body.replaceAll(" ","");
                String title=Title.getText().toString();
                title.trim();
                if(body.length() >=5)
                {
                    if(title.length()!=0 )
                    {
                        pd.setTitle("Posting..");
                        pd.show();
                        PostProcess(MainUri);
                    }
                    else
                    {
                        Toast.makeText(Post.this, "Check your Title", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(Post.this, "Your Vlog Content is too small", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!checkStoragePermission())
                {
                    requestStoragePermission();
                }
                else
                {
                    pickFromGallery();
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_REQUEST_CODE:{

                //picking from gallery ,first check if storage permission allowed or not
                if(grantResults.length >0){
                    boolean writeStorageAccepted =grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        //permission Enabled
                        pickFromGallery();
                    }else {
                        //permission denied
                        Toast.makeText(this, "please,Enable Storage Permission", LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    private void PostProcess(Uri uri)
    {
        String path=Fname ;
        final StorageReference ref=FirebaseStorage.getInstance().getReference(Fname);
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri=task.getResult();
                                HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("PostImageUri",uri.toString());
                        hashMap.put("Title",Title.getText().toString()+"");
                        hashMap.put("Content",Content.getText().toString()+"");
                        databaseReference.child(Title.getText().toString()).setValue(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        Toast.makeText(Post.this, "Posted..", LENGTH_SHORT).show();
                                        startActivity( new Intent( Post.this,DashBoard.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Post.this, ""+e.getMessage(), LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).
                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isSuccessful());
//                Uri downloadUri = uriTask.getResult();
//                //check image is uploaded or not or url is received
//                if(uriTask.isSuccessful())
//                {
//                        HashMap<String,Object> hashMap=new HashMap<>();
//                        hashMap.put("PostImageUri",downloadUri);
//                        hashMap.put("Title",Title.getText().toString()+"");
//                        hashMap.put("Content",Content.getText().toString()+"");
//                        databaseReference.child(Title.getText().toString()).setValue(hashMap)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if(task.isSuccessful()) {
//                                            pd.dismiss();
//                                            Toast.makeText(Post.this, "Posted", LENGTH_SHORT).show();
//                                            Intent intent=new Intent(Post.this,DashBoard.class);
//                                            startActivity(intent);
//                                        }
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        pd.dismiss();
//                                        Toast.makeText(Post.this, ""+e.getMessage(), LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
//                    else {
//                        Toast.makeText(Post.this, "" + downloadUri.toString(), LENGTH_SHORT).show();
//                        pd.dismiss();
//                    }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Post.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void upload(Uri uri)
    {
        MainUri=uri;
        Picasso.get().load(uri).placeholder(R.drawable.person).into(imageView);
    }

    private void pickFromGallery() {
        //pick from Gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                ImageURI = data.getData();
                upload(ImageURI);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void requestStoragePermission()
    {
        //request runtime storage permission
        requestPermissions(storagePerission,STORAGE_REQUEST_CODE);
    }
    private boolean checkStoragePermission(){
        //check storag permission is enable or not
        //return tru if enabled
        //return falseif not enabled
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
}