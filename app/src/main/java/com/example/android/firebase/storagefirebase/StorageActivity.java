package com.example.android.firebase.storagefirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.firebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StorageActivity extends AppCompatActivity {

    @BindView(R.id.btncapture)
    Button btncapture;
    @BindView(R.id.btnchoose)
    Button btnchoose;
    @BindView(R.id.btnupload)
    Button btnupload;
    @BindView(R.id.imgpreview)
    ImageView imgpreview;
    private Uri filepath;
    StorageReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        ButterKnife.bind(this);
        reference = FirebaseStorage.getInstance().getReference();
    }

    @OnClick({R.id.btncapture, R.id.btnchoose, R.id.btnupload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btncapture:
                break;
            case R.id.btnchoose:
                showfilechooser();
                break;
            case R.id.btnupload:
                upLoadFile();
                break;
        }
    }

    private void upLoadFile() {
        if (filepath!=null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("informasi");
            dialog.setMessage("loading");
            dialog.show();

            StorageReference riversRef = reference.child("images/profile.jpg");
            riversRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            dialog.dismiss();
                            Toast.makeText(StorageActivity.this, "file uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            dialog.dismiss();
                            Toast.makeText(StorageActivity.this, "gagal uploaded " + exception.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage(((int) progress) + "% Uploaded");
                        }
                    })
            ;
        }
    }

    private void showfilechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select an image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imgpreview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode==3&&resultCode==RESULT_OK){
            Uri uri = data.getData();
        }
    }
}