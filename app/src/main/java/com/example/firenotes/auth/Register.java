package com.example.firenotes.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firenotes.MainActivity;
import com.example.firenotes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {
    EditText rUserName, rUserEmail, rUserPass, rUserConfPass;
    Button syncAccount;
    TextView loginAct;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rUserName = findViewById(R.id.userName);
        rUserEmail = findViewById(R.id.useremail);
        rUserPass = findViewById(R.id.password);
        rUserConfPass = findViewById(R.id.passwordConfirm);

        syncAccount = findViewById(R.id.syncBtn);
        loginAct = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar4);

        fAuth = FirebaseAuth.getInstance();

        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, LogIn.class));
            }
        });

        syncAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uUsername = rUserName.getText().toString();
                String uUserEmail = rUserEmail.getText().toString();
                String uUserPass = rUserPass.getText().toString();
                String uConfPass = rUserConfPass.getText().toString();

                if (uUserEmail.isEmpty() || uUsername.isEmpty() || uUserPass.isEmpty() || uConfPass.isEmpty()){
                    Toast.makeText(Register.this, "All Fields Are Required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!uUserPass.equals(uConfPass)){
                    rUserConfPass.setError("Password Does Not Match");
                }

                progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider.getCredential(uUserEmail, uUserPass);
                fAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(Register.this, "Notes are Synced.", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = fAuth.getCurrentUser();
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uUsername)
                                .build();
                        user.updateProfile(request);

                        startActivity(new Intent(Register.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Failed to connect. Please try again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}