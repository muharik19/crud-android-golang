package com.example.helloimage.edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.helloimage.R;
import com.example.helloimage.ReadAllActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    //private static final String TAG = "EditActivity";
    private TextView IdMhs; //pembuatan variable view text
    private EditText Nim, Nama, No_Hp, Jurusan; //pembuatan variable edit text
    private ImageView imageView; //pembutan variable image
    private Button btnSimpan; //pembuatan variable button
    private ProgressDialog progressDialog; //pembuatan variable progress dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Ubah mahasiswa");

        IdMhs = findViewById(R.id.tvIdmhs); //inisialisasi value Id
        Nim = findViewById(R.id.etNim); //inisialisasi value Nim
        Nama = findViewById(R.id.etNama); //inisialisasi value Nama
        No_Hp = findViewById(R.id.etHp); //inisialisasi value No Hp
        Jurusan = findViewById(R.id.etJurusan); //inisialisasi value Jurusan
        imageView = findViewById(R.id.ivProf); //inisialisasi value image profile
        btnSimpan = findViewById(R.id.btnUbah); //inisialisasi value btnSimpan

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String nim = intent.getStringExtra("nim");
        String nama = intent.getStringExtra("nama");
        String hp = intent.getStringExtra("noHp");
        String jurusan = intent.getStringExtra("jurusan");
        String photo = intent.getStringExtra("photo");
        IdMhs.setText(id);
        //Log.d(TAG, "idmhs: " + id);
        Nim.setText(nim);
        Nama.setText(nama);
        No_Hp.setText(hp);
        Jurusan.setText(jurusan);
        Picasso.get().load(photo).into(imageView);

        progressDialog = new ProgressDialog(EditActivity.this);
        progressDialog.setMessage("Uploading Image. Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(EditActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                CropImage.activity()
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        .start(EditActivity.this);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if (permissionDeniedResponse.isPermanentlyDenied()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                    builder.setTitle("Permission Required")
                                            .setMessage("Permission to access your device storage is required to pick profile image. Please go to settings to enable permission to access storage")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                    startActivityForResult(intent, 51);
                                                }
                                            })
                                            .setNegativeButton("Cancel", null)
                                            .show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File imageFile = new File(resultUri.getPath()); //mengambil Value nama image
                        String id = IdMhs.getText().toString(); //mengambil Value Id menjadi string
                        String nim = Nim.getText().toString(); //mengambil Value Nama menjadi string
                        String nama = Nama.getText().toString(); //mengambil Value Nama menjadi string
                        String jurusan = Jurusan.getText().toString(); //mengambil Value Nama menjadi string
                        String no_hp = No_Hp.getText().toString(); //mengambil Value Nama menjadi string
                        progressDialog.show();
                        AndroidNetworking.upload("http://192.168.43.237:83/unpam/v1/mahasiswaupdate/")
                                .addMultipartFile("image", imageFile)
                                .addMultipartParameter("id", id)
                                .addMultipartParameter("nim", nim)
                                .addMultipartParameter("nama", nama)
                                .addMultipartParameter("jurusan", jurusan)
                                .addMultipartParameter("no_hp", no_hp)
                                .setPriority(Priority.HIGH)
                                .build()
                                .setUploadProgressListener(new UploadProgressListener() {
                                    @Override
                                    public void onProgress(long bytesUploaded, long totalBytes) {
                                        float progress = (float) bytesUploaded / totalBytes * 100;
                                        progressDialog.setProgress((int)progress);
                                    }
                                })
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Log.i("mytag", response);
                                        try {
                                            progressDialog.dismiss();
                                            JSONObject jsonObject = new JSONObject(response);
                                            int status = jsonObject.getInt("status");
                                            String message = jsonObject.getString("message");
                                            if (status == 0) {
                                                Toast.makeText(EditActivity.this, "Unable to upload image: " + message, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(EditActivity.this, message, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(EditActivity.this, ReadAllActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(EditActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        progressDialog.dismiss();
                                        // Log.d(TAG, "onError: Failed" + anError); //untuk log pada onerror
                                        // Log.d(TAG, "onError: Failed" + String.valueOf(anError.getErrorCode())); //untuk log pada onerror
                                        // Log.d(TAG, "onError: Failed" + anError.getErrorBody()); //untuk log pada onerror
                                        // Log.d(TAG, "onError: Failed" + anError.getErrorDetail()); //untuk log pada onerror
                                        anError.printStackTrace();
                                        Toast.makeText(EditActivity.this, "Error Uploading Image", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
