package com.uclk.shani.youseelk.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uclk.shani.youseelk.R;
import com.uclk.shani.youseelk.objects.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUp";

    //START declare Auth
    private FirebaseAuth mAuth;
    //END declare Auth

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;

    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        mEmailField = (EditText) findViewById(R.id.etSUEmail);
        mPasswordField = (EditText) findViewById(R.id.etSUPassword);
        mConfirmPasswordField = (EditText) findViewById(R.id.etSUConfirmPassword);

        Button next = (Button) findViewById(R.id.btnNextPref);
        Button signUpFirst = (Button) findViewById(R.id.btnSignUpWithoutPref);
        Button hasAcc = (Button) findViewById(R.id.btnHasAccount);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PreferencesActivity.class));
            }
        });

        signUpFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(mEmailField.getText().toString(),mPasswordField.getText().toString());
            }
        });

        hasAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }


    //validating email and password fields
    protected boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        String confirm = mConfirmPasswordField.getText().toString();

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
            valid = validatePassword(password,confirm);

        }
        return valid;
    }


    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, send verification email
                            Log.d(TAG, "createUserWithEmail:success");
                            sendEmailVerification(email,password);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }


    //in order to verify the email address of the user
    private void sendEmailVerification(final String email, final String password) {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) throw new AssertionError();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                            //START save the user in database
                            User users = new User(email,password);
                            DatabaseReference userRef = rootRef.child("users");
                            userRef.push().setValue(users);
                            //END save the user in database

                            //redirect to main activity
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getApplicationContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validatePassword(String password, String confirm){
        boolean valids = true;
        //confirm password
        if (TextUtils.isEmpty(confirm)) {
            mConfirmPasswordField.setError("Required.");
            valids = false;
        } else if (!password.equals(confirm)) {
            mConfirmPasswordField.setError("Not matching.");
            valids = false;
        } else {
            mConfirmPasswordField.setError(null);
        }
        return valids;
    }



}
