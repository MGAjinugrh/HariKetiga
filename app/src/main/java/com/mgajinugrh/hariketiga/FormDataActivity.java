package com.mgajinugrh.hariketiga;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText namaAsisten, tahunAngkatan;
    private Button simpanBtn;

    private DatabaseReference mDatabaseAsistenRef;

    private String action, key;
    private AsistenModel asistenModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_data);

        asistenModel = new AsistenModel();
        mDatabaseAsistenRef = FirebaseDatabase.getInstance().getReference("listAsisten");

        namaAsisten = findViewById(R.id.tv_namaform);
        tahunAngkatan = findViewById(R.id.tv_tahunform);
        simpanBtn = findViewById(R.id.btn_simpan);

        Bundle extras = getIntent().getExtras();

        try{
            key = extras.getString("KEY");
            namaAsisten.setText(extras.getString("NAMA_ASISTEN"));
            tahunAngkatan.setText(extras.getString("TAHUN_ANGKATAN"));
            action = extras.getString("ACTION");
        }catch (NullPointerException e){
            action = "tambah";
        }

        simpanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String asisten = namaAsisten.getText().toString();
        String tahun = tahunAngkatan.getText().toString();

        if (!isEmpty(asisten) && !isEmpty(tahunAngkatan.getText().toString())) {
            asistenModel = new AsistenModel(asisten, tahun);
            if (action.equals("tambah")) {
                mDatabaseAsistenRef.push().setValue(asistenModel).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(R.id.btn_simpan), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                    }
                });
            } else if (action.equals("edit")) {
                mDatabaseAsistenRef.child(key).setValue(asistenModel).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(R.id.btn_simpan), "Data berhasil diupdate", Snackbar.LENGTH_LONG).show();
                    }
                });
            }

            namaAsisten.setText("");
            tahunAngkatan.setText("");
            namaAsisten.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view2 = getCurrentFocus();
            if (view2 == null) {
                view2 = new View(FormDataActivity.this);
            }
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        } else
            Snackbar.make(findViewById(R.id.btn_simpan), "Lengkapi data terlebih dahulu", Snackbar.LENGTH_LONG).show();


    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }
}
