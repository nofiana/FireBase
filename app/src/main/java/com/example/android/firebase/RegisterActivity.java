package com.example.android.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.sign_up_button)
    Button signUpButton;
    @BindView(R.id.btn_reset_password)
    Button btnResetPassword;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //inisialisasi firebase auth
        auth=FirebaseAuth.getInstance();
        listener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    Toast.makeText(RegisterActivity.this, "cek email untuk verifikasi", Toast.LENGTH_SHORT).show();
                    verifikasiemail();
                }
            }
        };
    }

    @OnClick({R.id.sign_up_button, R.id.btn_reset_password, R.id.sign_in_button})
    public void onViewClicked(View view) {

        String em = email.getText().toString();
        String pw =  password.getText().toString();

        switch (view.getId()) {
            case R.id.sign_up_button:
                if (TextUtils.isEmpty(em)) {
                    email.setError("email harus diisi");
                    email.requestFocus();
                   
                } else if (TextUtils.isEmpty(pw)) {
                    password.setError("passowrd harus diisi");
                    password.requestFocus();
                    
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(em,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //mengecek kondisi apakah berhasil atau gagal register
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "berhasil register", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "gagal register"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    }).addOnFailureListener(new OnFailureListener() { //JIKA GAGAL DILUAR DARI CASE
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "gagal"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            
                        }
                    });
                }
                    



                break;
            case R.id.btn_reset_password:
                break;
            case R.id.sign_in_button:

                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }

    //unruk mengirimkan verifikasi ke email user yang terdaftar unruk ferivikasi
    private void verifikasiemail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();
                } else {
                    //restart activity
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(listener);
    }
}
