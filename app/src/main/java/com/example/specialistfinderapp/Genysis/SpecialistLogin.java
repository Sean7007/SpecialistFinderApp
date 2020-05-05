package com.example.specialistfinderapp.Genysis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.example.specialistfinderapp.MainActivity;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SpecialistLogin extends AppCompatActivity {
    //Variable Declaration
    Button login, back;
    EditText email, password;
    TextView register;
    TextView forgot;
    AwesomeValidation awesomeValidation;
    FirebaseAuth mAuth; //declaration of fire-base
    private FirebaseAuth.AuthStateListener firebaseAuthListener; //State Listener of FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_login);

        mAuth = FirebaseAuth.getInstance(); //Retrieves state of Fire-base
        login = findViewById(R.id.login);
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registrationTextView);
        forgot = findViewById(R.id.forgotPasswordTextView);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //Validate the confirmation of fields
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        //Validation of email
        awesomeValidation.addValidation(SpecialistLogin.this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerr);
        awesomeValidation.addValidation(SpecialistLogin.this, R.id.password, regexPassword, R.string.passerr);



            firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = mAuth.getCurrentUser();//This stores the state of the currently logged in user
                    if (user != null) {
                        Intent intent = new Intent(SpecialistLogin.this, SpecialistHome.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(SpecialistLogin.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(SpecialistLogin.this, "Please Login!", Toast.LENGTH_SHORT).show();
                    }
                }//End of onAuthStateChanged
            };

            //Set onClickListener of the login-button
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Authenticating variables
                    String cEmail = email.getText().toString();
                    String cPassword = password.getText().toString();

                    //If validations are met
                    if (awesomeValidation.validate()) {
                        //Authenticating section
                        mAuth.signInWithEmailAndPassword(cEmail, cPassword)
                                .addOnCompleteListener(SpecialistLogin.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SpecialistLogin.this, "Sign-in Failure!Try Again...", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SpecialistLogin.this, "Data Received Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SpecialistLogin.this, SpecialistHome.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(SpecialistLogin.this, "O Ja ERROR MR!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //Set onClickListener of the register
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SpecialistLogin.this, SpecialistRegi.class);
                    startActivity(intent);
                    finish();
                    return;

                }
            });

            //Set onClickListener of the register
            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SpecialistLogin.this, SpecialistForgot.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            });

            //Set onClickListener of the register
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SpecialistLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            });

        }

        @Override
        protected void onStart () {
            super.onStart();
            mAuth.addAuthStateListener(firebaseAuthListener);
        }

        @Override
        protected void onStop () {
            super.onStop();
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }


}//End of class
