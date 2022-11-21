package com.example.cce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView register;
    private EditText editTextFullName,editTextAge,editTextEmail,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();


        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner2:
                startActivity(new Intent(this,MainActivity.class));
                break;
                case R.id.register:
                    register();
                    break;

        }

    }

    private void register() {
        String email = editTextEmail.getText().toString().trim();
        String fullname = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (fullname.isEmpty()) {
            editTextFullName.setError("Full Name is required");
            editTextFullName.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            editTextAge.setError("Age is required");
            editTextAge.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email  is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please Provide Valid Email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min Password must be 6 character !! ");
            editTextPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(fullname, age, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(register.this , "User have been registered succesfully ", Toast.LENGTH_LONG).show();
                                    }else Toast.makeText(register.this , "Failed to Register !! Try again ! ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            Toast.makeText(register.this , "Failed to Register !! Try again ! ", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        }
    }
