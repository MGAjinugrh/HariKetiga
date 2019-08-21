package com.mgajinugrh.hariketiga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DaftarActivity extends AppCompatActivity {

    private TextInputEditText txtNama, txtEmail, txtPassword;
    private Button btnDaftar;
    private ProgressDialog progressDialog;

    private String uID;
    private String nama, email, password;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUserRef = FirebaseDatabase.getInstance().getReference("users");

        txtNama = findViewById(R.id.tv_namadaftar);
        txtEmail = findViewById(R.id.tv_emaildaftar);
        txtPassword = findViewById(R.id.tv_passworddaftar);
        btnDaftar = findViewById(R.id.btn_daftar);
        progressDialog = new ProgressDialog(DaftarActivity.this);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validasiData();
                buatAkun();
            }
        });
    }

    private void validasiData() {
        // cek apakah semua data udah keisi
        nama = txtNama.getText().toString();
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();

        if (!isEmpty(nama) && !isEmpty(email) && !isEmpty(password)) {
            progressDialog.setTitle("Membuat Akun");
            progressDialog.setMessage("Tunggu sebentar, akun Anda sedang dibuat");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            buatAkun();
        } else
            Snackbar.make(findViewById(R.id.btn_daftar), "Lengkapi profil Anda", Snackbar.LENGTH_LONG).show();

    }

    private void buatAkun() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                    uID = mCurrentUser.getUid();

                    simpanUser(new UserModel(nama, email));
                } else {
                    Toast.makeText(DaftarActivity.this, "Gagal membuat akun. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }

    private void simpanUser(UserModel user) {
        mDatabaseUserRef.child(uID).setValue(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DaftarActivity.this, "Berhasil Daftar", Toast.LENGTH_SHORT).show();
                uID = "";
                FirebaseAuth.getInstance().signOut();
                Intent pindahLogin = new Intent(DaftarActivity.this, MainActivity.class);
                startActivity(pindahLogin);
            }
        });

    }
}