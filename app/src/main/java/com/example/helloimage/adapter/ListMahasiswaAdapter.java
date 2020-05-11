package com.example.helloimage.adapter;

import android.app.AlertDialog;
import android.content.Context;
//import android.util.Log;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.helloimage.CustomOnItemClickListener;
import com.example.helloimage.R;
import com.example.helloimage.data.DataMahasiswa;
import com.example.helloimage.edit.EditActivity;
import com.example.helloimage.read.ReadAllActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ListMahasiswaAdapter extends RecyclerView.Adapter<ListMahasiswaAdapter.ViewHolder> {

    private static final String TAG = "ReadAllActivity";
    private Context context;
    private List<DataMahasiswa> dataMahasiswa; // inisialisasi List dengan object DataMahasiswa
    private Intent checknama, checkid, checkphoto, checkjurusan, checkhp, checknim;

    // construktor ListMahasiswaAdapter
    public ListMahasiswaAdapter(Context context, List<DataMahasiswa> dataMahasiswa) {
        this.dataMahasiswa = dataMahasiswa;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate view yang akan digunakan yaitu layout list_mahasiswa_row.xml
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mahasiswa_row, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    } // fungsi yang dijalankan saat ViewHolder dibuat

    // Note harus sesuai urutan yang ada di ReadAllActivity.java:
    // dataMahasiswa.add(new DataMahasiswa(
    //  data.getInt("id"),
    //  data.getString("photo"),
    //  data.getString("nama"),
    //  data.getString("nim"),
    //  data.getString("jurusan"),
    //  data.getString("noHp")
    // ));
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataMahasiswa data = dataMahasiswa.get(position); // inisialisasi object DataMahasiwa
        Glide.with(context)
                .load(data.getPhotoMahasiswa())
                .apply(new RequestOptions().override(350, 550))
                .into(holder.mPhoto);
        // Log.d(TAG, "photo: " + data.getPhotoMahasiswa());
        holder.mNama.setText(data.getNamaMahasiswa()); // menset value view "mNama" sesuai data dari getNamaMahasiswa();
        // Log.d(TAG, "nama: " + data.getNamaMahasiswa());
        holder.mNim.setText(data.getNimMahasiswa()); // menset value view "mNim" sesuai data dari getNimMahasiswa();
        // Log.d(TAG, "nim: " + data.getNimMahasiswa());
        holder.mJurusan.setText(data.getJurusanMahasiswa()); // menset value view "mNim" sesuai data dari getJurusanMahasiswa();
        // Log.d(TAG, "jurusan: " + data.getJurusanMahasiswa());
        holder.mHp.setText(data.getNoHpMahasiswa()); // menset value view "mNim" sesuai data dari getNoHpMahasiswa();
        // Log.d(TAG, "noHp: " + data.getNoHpMahasiswa());
        holder.btnUpdate.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                //Toast.makeText(context, "Update "+ getListMahasiswa().get(position).getIdMahasiswa(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, EditActivity.class);
                //intent.putExtra("id", getListMahasiswa().get(position).getIdMahasiswa());
                //intent.putExtra("nama", getListMahasiswa().get(position).getNamaMahasiswa());
                DataMahasiswa listmhs = dataMahasiswa.get(position);
                //int idmhs = getListMahasiswa().get(position).getIdMahasiswa();
                checkid = intent.putExtra("id", String.valueOf(listmhs.getIdMahasiswa()));
                //Log.d(TAG, "intentid: " + checkid.getIntExtra("id", listmhs.getIdMahasiswa()));
                checknama = intent.putExtra("nama", listmhs.getNamaMahasiswa());
                //Log.d(TAG, "intentname: " + checknama.getStringExtra("nama"));
                checkphoto = intent.putExtra("photo", listmhs.getPhotoMahasiswa());
                //Log.d(TAG, "intentphoto: " + checkphoto.getStringExtra("photo"));
                checkjurusan = intent.putExtra("jurusan", listmhs.getJurusanMahasiswa());
                //Log.d(TAG, "intentjurusan: " + checkjurusan.getStringExtra("jurusan"));
                checkhp = intent.putExtra("noHp", listmhs.getNoHpMahasiswa());
                //Log.d(TAG, "intenthp: " + checkhp.getStringExtra("noHp"));
                checknim = intent.putExtra("nim", listmhs.getNimMahasiswa());
                //Log.d(TAG, "intentnim: " + checknim.getStringExtra("nim"));
                context.startActivity(intent);
            }
        }));

        holder.btnDelete.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                final DataMahasiswa listmhsid = dataMahasiswa.get(position);
                final int id = listmhsid.getIdMahasiswa();
                //Toast.makeText(context, "Delete "+ listmhsid.getIdMahasiswa(), Toast.LENGTH_SHORT).show();
                //https://stackoverflow.com/questions/53716271/alertdialog-builder-inside-holder-itemview-setonclicklistener-onbindviewholder
                new AlertDialog.Builder(view.getContext()) // problem over here
                        .setTitle("Delete Mahasiswa")
                        .setMessage("Are you sure you want to delete ? " + listmhsid.getNamaMahasiswa())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AndroidNetworking.post("http://192.168.43.237:83/unpam/v1/mahasiswadelete/")
                                        .addBodyParameter("id", String.valueOf(id))
                                        .setPriority(Priority.LOW)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    //Log.d(TAG, "onResponse: " + response);
                                                    String message = response.getString("message");
                                                    //Log.d(TAG, "BALALALA: " + message);
                                                    Intent intent = new Intent(context, ReadAllActivity.class);
                                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                    context.startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(context, "Parsing Error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onError(ANError error) {
                                                // handle error
                                                error.printStackTrace();
                                                Toast.makeText(context, "Error delete mahasiswa", Toast.LENGTH_SHORT).show();
                                                //Log.d(TAG, "onError: Failed" + error); //untuk log pada onerror
                                                //Log.d(TAG, "onError: Failed" + String.valueOf(error.getErrorCode())); //untuk log pada onerror
                                                //Log.d(TAG, "onError: Failed" + error.getErrorBody()); //untuk log pada onerror
                                                //Log.d(TAG, "onError: Failed" + error.getErrorDetail()); //untuk log pada onerror
                                            }
                                        });
                                //Toast.makeText(context, "Delete "+ listmhsid.getNamaMahasiswa(), Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No",null).show();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return dataMahasiswa.size(); //mengambil item sesuai urutan
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;
        TextView mNama, mNim, mJurusan, mHp; //inisialisasi variabel
        Button btnUpdate, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.img_item_photo); // find layout sesuai dengan yg di list_mahasiswa_row.xml
            mNama = itemView.findViewById(R.id.tv_item_nama); // find layout sesuai dengan yg di list_mahasiswa_row.xml
            mNim = itemView.findViewById(R.id.tv_item_nim); // find layout sesuai dengan yg di list_mahasiswa_row.xml
            mJurusan = itemView.findViewById(R.id.tv_item_jurusan); // find layout sesuai dengan yg di list_mahasiswa_row.xml
            mHp = itemView.findViewById(R.id.tv_item_hp); // find layout sesuai dengan yg di list_mahasiswa_row.xml
            btnUpdate = itemView.findViewById(R.id.btn_update); // find layout sesuai dengan yg di list_mahasiswa_row.xml
            btnDelete = itemView.findViewById(R.id.btn_delete); // find layout sesuai dengan yg di list_mahasiswa_row.xml
        }
    }
}
