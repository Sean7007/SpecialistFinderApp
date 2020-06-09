package com.example.specialistfinderapp.Genysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.specialistfinderapp.MainActivity;
import com.example.specialistfinderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class CustomerLogin extends AppCompatActivity  {
    //Variable Declaration
    Button login, back;
    private android.widget.EditText cemail, cpassword, phoneNumber;
    AppCompatCheckBox checkBox;
    TextView register;
    TextView forgot;
    AwesomeValidation awesomeValidation;
    FirebaseUser firebaseUser;//Firebase User
    FirebaseAuth mAuth; //declaration of firebase
    private FirebaseAuth.AuthStateListener firebaseAuthListener; //State Listener of FirebaseAuth
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        mAuth = FirebaseAuth.getInstance(); //Retrieves state of Fire-base
        login= findViewById(R.id.login);
        back = findViewById(R.id.back);
        checkBox = findViewById(R.id.checkbox);
        cemail= findViewById(R.id.cemail);
        cpassword= findViewById(R.id.cpassword);
        register= findViewById(R.id.registrationTextView);
        forgot= findViewById(R.id.forgotPasswordTextView);

        //Validation conditions
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //Validate the confirmation of fields
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(CustomerLogin.this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerr);//Validation of email
        awesomeValidation.addValidation(CustomerLogin.this, R.id.password, regexPassword, R.string.passerr);


        //==========================================FIRE DATABASE=======================================================//

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = mAuth.getCurrentUser();//This stores the state of the currently logged in user
            if(user != null){
                Toast.makeText(CustomerLogin.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerLogin.this, Customer_Home.class);
                startActivity(intent);
                finish();
                return;
             }else{
                Toast.makeText(CustomerLogin.this, "Please Login!", Toast.LENGTH_SHORT).show();
                  }
            }//End of onAuthStateChanged
        };
        //============================================end FIRE DATABASE============================================================//

        //Set onClickListener of the login-button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Authenticating variables
                String email = cemail.getText().toString();
                String password = cpassword.getText().toString();

                //If validations are met-begin data authentication
                if(awesomeValidation.validate()){
                //Authenticating section
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CustomerLogin.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()){
                                                Toast.makeText(CustomerLogin.this, "Sign-in Failure!Try Again...", Toast.LENGTH_SHORT).show();
                                            }else{
                                            Toast.makeText(CustomerLogin.this, "Sign-in Success!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CustomerLogin.this, Customer_Home.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                    }
                        });
                }
                //Else if Validations are not met
                else{
                    Toast.makeText(CustomerLogin.this, "O Ja ERROR MR!", Toast.LENGTH_SHORT).show();
                }
            }
        });//End of login.setOnClick

        //Set onClickListener of the register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLogin.this, CustomerRegi.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        //Set onClickListener of the back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLogin.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


        //Set onClickListener of the forget
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerLogin.this, CustomerForgot.class);
                startActivity(i);
                finish();
                return;
            }
        });

        //Hides password
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

    }//End of onCreate

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Checks if user is null
        if(firebaseUser != null){
            Intent intent = new Intent(CustomerLogin.this,Customer_Home.class );
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }



}//End of class

