package com.example.helloimage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText Nim, Nama, No_Hp, Jurusan; //pembuatan variable edit text
    private ImageView imageView; //pembutan variable image
    private Button btnSimpan; //pembuatan variable button
    private ProgressDialog progressDialog; //pembuatan variable progress dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Buat mahasiswa baru");

        Nim = findViewById(R.id.etNim); //inisialisasi value Nim
        Nama = findViewById(R.id.etNama); //inisialisasi value Nama
        No_Hp = findViewById(R.id.etHp); //inisialisasi value Kelas
        Jurusan = findViewById(R.id.etJurusan); //inisialisasi value Jurusan
        imageView = findViewById(R.id.ivProf); //inisialisasi value image profile
        btnSimpan = findViewById(R.id.btnTambah); //inisialisasi value btnSimpan

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Uploading Image. Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                CropImage.activity()
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        .start(MainActivity.this);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if (permissionDeniedResponse.isPermanentlyDenied()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        String nim = Nim.getText().toString(); //mengambil Value Nama menjadi string
                        String nama = Nama.getText().toString(); //mengambil Value Nama menjadi string
                        String jurusan = Jurusan.getText().toString(); //mengambil Value Nama menjadi string
                        String no_hp = No_Hp.getText().toString(); //mengambil Value Nama menjadi string
                        progressDialog.show();
                        AndroidNetworking.upload("http://192.168.43.237:83/unpam/v1/mahasiswaadd/")
                                .addMultipartFile("image", imageFile)
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
                                                Toast.makeText(MainActivity.this, "Unable to upload image: " + message, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(MainActivity.this, ReadAllActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(MainActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(MainActivity.this, "Error Uploading Image", Toast.LENGTH_SHORT).show();
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
