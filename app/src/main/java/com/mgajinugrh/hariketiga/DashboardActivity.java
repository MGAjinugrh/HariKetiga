package com.mgajinugrh.hariketiga;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private LinearLayout btnTambah, emptyData;
    private RecyclerView rvAsisten;
    private ImageButton btnKeluar;

    private DatabaseReference mDatabaseAsistenRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDatabaseAsistenRef = FirebaseDatabase.getInstance()
                .getReference("listAsisten");

        btnTambah = findViewById(R.id.btn_tambahdata);
        btnKeluar = findViewById(R.id.btn_keluar);
        emptyData = findViewById(R.id.view_emptydata);
        rvAsisten = findViewById(R.id.rv_dataasisten);

        rvAsisten.setNestedScrollingEnabled(false);
        rvAsisten.setHasFixedSize(false);
        rvAsisten.setLayoutManager(new
                LinearLayoutManager(DashboardActivity.this));

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent form = new Intent(
                        DashboardActivity.this,
                        FormDataActivity.class);
                startActivity(form);
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent signOut = new Intent(
                        DashboardActivity.this,MainActivity.class
                );
                finish();
            }
        });

        //nampilin data asisten ke recycler view
        fetchDataAsisten();
    }

    private void fetchDataAsisten() {
        //adapter berfungsi untuk menghubungkan data dengan recyclerview
        FirebaseRecyclerAdapter<AsistenModel, AsistenViewHolder>
                userRecycleAdapter = new FirebaseRecyclerAdapter<AsistenModel,
                AsistenViewHolder>(
                AsistenModel.class,
                R.layout.single_list_item,
                AsistenViewHolder.class,
                mDatabaseAsistenRef ) {
            @Override
            protected void populateViewHolder(final AsistenViewHolder viewHolder, final AsistenModel model, int position) {

                // ambil key (uid) tiap data asisten
                final String list_key = getRef(position).getKey();

                // buat ngecek ada data asisten atau engga di db
                mDatabaseAsistenRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            emptyData.setVisibility(View.GONE);
                        } else {
                            emptyData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                // ambil data asistennya dan nampilin di recycler view
                mDatabaseAsistenRef.child(list_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            final String key_id = dataSnapshot.getKey();
                            final String asisten = dataSnapshot.child("namaAsisten").getValue().toString();
                            final String tahun = dataSnapshot.child("tahunAngkatan").getValue().toString();

                            viewHolder.setNama(asisten);
                            viewHolder.setTahun(tahun);

                            AsistenModel asistenModel = new AsistenModel(asisten, tahun, key_id);

                            // pindah ke form data buat edit dengan bawa data (putExtra)
                            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent pindah = new Intent(DashboardActivity.this, FormDataActivity.class);
                                    pindah.putExtra("KEY", key_id);
                                    pindah.putExtra("NAMA_ASISTEN", asisten);
                                    pindah.putExtra("TAHUN_ANGKATAN", tahun);
                                    pindah.putExtra("ACTION", "edit");
                                    startActivity(pindah);
                                }
                            });

                            // ngapus data asisten
                            viewHolder.hapus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mDatabaseAsistenRef.child(list_key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(DashboardActivity.this, "Data berhasil dihapus", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        };

        // set adapter untuk recycler view
        rvAsisten.setAdapter(userRecycleAdapter);
        }

        public static class AsistenViewHolder extends RecyclerView.ViewHolder{
            public View mView;
            public TextView namaView, tahunView;
            public ImageButton edit, hapus;

            public AsistenViewHolder(View itemView){
                super(itemView);

                mView = itemView;

                namaView = mView.findViewById(R.id.item_nama);
                tahunView = mView.findViewById(R.id.item_angkatan);
                edit = mView.findViewById(R.id.item_btnedit);
                hapus = mView.findViewById(R.id.item_btnhapus);
            }

            public void setNama(String n) {
                namaView.setText(n);
            }

            public void setTahun(String n) {
                tahunView.setText(n);
            }
        }
    }
