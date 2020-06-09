package com.example.specialistfinderapp.Genysis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.specialistfinderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerRegi extends AppCompatActivity {
    //Variable Declaration
    Button register, back;
    EditText fname, lname, cemail,cphone, password, confPassword;
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth; //declaration of firebase
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_regi);

        //Variable Decalaration
        register= findViewById(R.id.register);
        back= findViewById(R.id.back);
        fname= findViewById(R.id.fname);
        lname= findViewById(R.id.lname);
        cemail= findViewById(R.id.cemail);
        cphone= findViewById(R.id.cphoneNo);
        password= findViewById(R.id.password);
        confPassword= findViewById(R.id.confPassword);

        mAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //Validate the confirmation of fields
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(CustomerRegi.this, R.id.fname, "[a-zA-Z\\s]+", R.string.fnameerr);
        awesomeValidation.addValidation(CustomerRegi.this, R.id.lname, "[a-zA-Z\\s]+", R.string.lnameerr);
        //Validation of email
        awesomeValidation.addValidation(CustomerRegi.this, R.id.cemail, Patterns.EMAIL_ADDRESS, R.string.emailerr);
        awesomeValidation.addValidation(CustomerRegi.this, R.id.cphoneNo, RegexTemplate.TELEPHONE, R.string.phoneerr);
        awesomeValidation.addValidation(CustomerRegi.this, R.id.password, regexPassword, R.string.passerr);
        awesomeValidation.addValidation(CustomerRegi.this, R.id.confPassword, R.id.password, R.string.cnfpasserr);

        //Set onClickListener of the login-button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Authenticating variables
                 String firstname = fname.getText().toString();
                 String lastname = lname.getText().toString();
                 String email = cemail.getText().toString();
                 String phone = cphone.getText().toString();
                 String cPassword = password.getText().toString();
                String cConfPassword = confPassword.getText().toString();

                //If validations are met-begin data authentication
                if(awesomeValidation.validate()){
                    //Authenticating section
                    mAuth.createUserWithEmailAndPassword(email, cPassword)
                            .addOnCompleteListener(CustomerRegi.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(CustomerRegi.this, ("Registration Failure! Try Again..."), Toast.LENGTH_SHORT).show();
                                    }else{
                                        //If successful print
                                        Toast.makeText(CustomerRegi.this, "Registration Success!", Toast.LENGTH_SHORT).show();
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        String userid = firebaseUser.getUid();

                                        reference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(userid);

                                       HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", userid);
                                        hashMap.put("firstname", firstname );
                                        hashMap.put("lastname", lastname);
                                        hashMap.put("email", email);
                                        hashMap.put("phone", phone);

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                              if(task.isSuccessful()){
                                                  //Move to next frame
                                                  Intent intent = new Intent(CustomerRegi.this, CustomerLogin.class);
                                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                  startActivity(intent);
                                                  finish();
                                                  return;
                                              }
                                            }
                                        });

                                    }
                                    }//End of onComplete
                            });//End of addOnComplete
                }else{
                    Toast.makeText(CustomerRegi.this, "O Ja ERROR MR!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set onClickListener of the register
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegi.this, CustomerLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            //Handle already logged in user

        }
    }
}
