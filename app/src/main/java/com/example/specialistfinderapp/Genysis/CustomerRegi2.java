package com.example.specialistfinderapp.Genysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.specialistfinderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CustomerRegi2 extends AppCompatActivity {
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth; //declaration of firebase

    private static final String TAG = CustomerRegi2.class.getSimpleName();

    private AppCompatEditText usernameBox, emailBox, passwordBox;
    CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);

        //init
        userRef = FirebaseFirestore.getInstance().collection("Users");
        mAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //Validate the confirmation of fields
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(CustomerRegi2.this, R.id.username, "[a-zA-Z\\s]+", R.string.fnameerr);
        //Validation of email
        awesomeValidation.addValidation(CustomerRegi2.this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerr);
        //awesomeValidation.addValidation(CustomerRegi.this, R.id.phoneNo, RegexTemplate.TELEPHONE, R.string.phoneerr);
        awesomeValidation.addValidation(CustomerRegi2.this, R.id.password, regexPassword, R.string.passerr);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_customer_regi2);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }


        AppCompatTextView signUpLink = (AppCompatTextView) findViewById(R.id.signup_link);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(CustomerRegi2.this, CustomerLogin.class);
                startActivity(loginIntent);
            }
        });


        usernameBox = (AppCompatEditText) findViewById(R.id.username);
        emailBox = (AppCompatEditText) findViewById(R.id.email);
        passwordBox = (AppCompatEditText) findViewById(R.id.password);


        AppCompatButton signUpBtn = (AppCompatButton) findViewById(R.id.signup_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Objects.requireNonNull(usernameBox.getText()).toString();
                String password = Objects.requireNonNull(passwordBox.getText()).toString();
                String email = Objects.requireNonNull(emailBox.getText()).toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    Toast.makeText(CustomerRegi2.this, "Login Fields must not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    //todo authenticate with Firestore
                    //Authenticating section
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "success");

                                        Intent intent = new Intent(CustomerRegi2.this, CustomerLogin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(CustomerRegi2.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }


            }
        });

    }

    }

