package com.mgajinugrh.hariketiga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText edtEmail, edtPass;
    private Button btnMasuk;
    private TextView btnDaftar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.tv_email);
        edtPass = findViewById(R.id.tv_pass);
        btnMasuk = findViewById(R.id.btn_masuk);
        btnDaftar = findViewById(R.id.tv_daftar);
        progressDialog = new ProgressDialog(MainActivity.this);

        btnMasuk.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.btn_masuk){
            signIn();
        }else if(i == R.id.tv_daftar){
            signUp();
        }
    }

    private void signUp(){
        Intent daftar = new Intent(MainActivity.this,DaftarActivity.class);
        startActivity(daftar);
    }

    private void signIn(){
        String email = edtEmail.getText().toString();
        final String password = edtPass.getText().toString();

        if((email.isEmpty() && password.isEmpty()) || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill your email and password",Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Waiting...");
            progressDialog.setMessage("Processing log in");
            progressDialog.setCancelable(false);
            progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent pindah = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(pindah);
                                finish();
                            } else {
                                if (task.getException().getMessage().contains("password is invalid")) {
                                    Toast.makeText(MainActivity.this, "Wrong password, please try again.", Toast.LENGTH_LONG).show();
                                    edtPass.requestFocus();
                                } else {
                                    Toast.makeText(MainActivity.this, "Email is not sign up yet.", Toast.LENGTH_LONG).show();
                                    edtEmail.requestFocus();
                                }

                                progressDialog.dismiss();
                            }
                        }
                    });


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*Ketika aplikasi dibuka cek apakah
         *dalam keadaan login
         */
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent dashboard = new Intent(MainActivity.this,DashboardActivity.class);
            startActivity(dashboard);
            finish();
        }
    }
}
