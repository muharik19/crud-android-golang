package com.example.helloimage.read;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.helloimage.adapter.ListMahasiswaAdapter;
import com.example.helloimage.create.CreateActivity;
import com.example.helloimage.R;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.helloimage.data.DataMahasiswa;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReadAllActivity extends AppCompatActivity {

    //inisialisasi variabel
    private static final String TAG = "ReadAllActivity";
    private List<DataMahasiswa> dataMahasiswa;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_all);

        recyclerView = findViewById(R.id.recyclerReadAllData); //findId recyclerView yg ada pada activity_read_all.xml
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        recyclerView.setHasFixedSize(true); //agar recyclerView tergambar lebih cepat
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //menset layout manager sebagai LinearLayout(scroll kebawah)
        dataMahasiswa = new ArrayList<>(); //arraylist untuk menyimpan data mahasiswa
        getData(); // pemanggilan fungsi get data
        fabAdd = findViewById(R.id.fab_add); //inisialisasi value tambah circle button

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReadAllActivity.this, CreateActivity.class));
            }
        });
    }

    public void getData() {
        // koneksi ke file read_all.php, jika menggunakan localhost gunakan ip sesuai dengan ip kamu
        AndroidNetworking.get("http://192.168.43.237:83/unpam/v1/mahasiswa/")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject results = response.getJSONObject("results");
                            // Log.d("listmhs", results.getString("list_mahasiswa"));
                            JSONArray listMahasiswa = results.getJSONArray("list_mahasiswa");
                            // Log.d(TAG, "LIST: " + listMahasiswa);
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < listMahasiswa.length(); i++) {
                                JSONObject data = listMahasiswa.getJSONObject(i);
                                String mhs_id = data.getString("id");
                                //Log.d(TAG, "idmhs: " + mhs_id);
                                String mhs_nim = data.getString("nim");
                                //Log.d(TAG, "nimmhs: " + mhs_nim);
                                String mhs_nama = data.getString("nama");
                                //Log.d(TAG, "namamhs: " + mhs_nama);
                                String mhs_hp = data.getString("noHp");
                                //Log.d(TAG, "hpmhs: " + mhs_hp);
                                String mhs_jurusan = data.getString("jurusan");
                                //Log.d(TAG, "jrsmhs: " + mhs_jurusan);
                                String mhs_photo = data.getString("photo");
                                //Log.d(TAG, "phtmhs: " + mhs_photo);
                                //adding the product to product list
                                dataMahasiswa.add(new DataMahasiswa(
                                        data.getInt("id"), //"name:/String" diisi sesuai dengan yang di JSON pada read_all
                                        data.getString("photo"), //"name:/String" diisi sesuai dengan yang di JSON pada read_all
                                        data.getString("nama"), //"name:/String" diisi sesuai dengan yang di JSON pada read_all
                                        data.getString("nim"), //"name:/String" diisi sesuai dengan yang di JSON pada read_all
                                        data.getString("jurusan"), //"name:/String" diisi sesuai dengan yang di JSON pada read_all
                                        data.getString("noHp") //"name:/String" diisi sesuai dengan yang di JSON pada read_all
                                ));
                                // Log.d(TAG, "listdatamhs: " + dataMahasiswa);
                            }

                            //men inisialisasi adapter RecyclerView yang sudah kita buat sebelumnya
                            ListMahasiswaAdapter adapter = new ListMahasiswaAdapter(ReadAllActivity.this, dataMahasiswa);
                            recyclerView.setAdapter(adapter); //menset adapter yang akan digunakan pada recyclerView
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError); //untuk log pada onerror
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }
}