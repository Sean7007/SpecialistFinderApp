package com.example.specialistfinderapp.Genysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.User;
import com.example.specialistfinderapp.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SpecialistRegi extends AppCompatActivity {
    //Variable Declaration
    Button register, back;
    EditText fname, lname, email, phone, password, confPassword;
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth; //declaration of firebase
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_regi);

        //Variable Decalaration
        register= findViewById(R.id.register);
        back= findViewById(R.id.back);
        fname= findViewById(R.id.fname);
        lname= findViewById(R.id.lname);
        email= findViewById(R.id.email);
        phone = findViewById(R.id.phoneNo);
        password= findViewById(R.id.password);
        confPassword= findViewById(R.id.confPassword);

        mAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //Validate the confirmation of fields
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(SpecialistRegi.this, R.id.fname, "[a-zA-Z\\s]+", R.string.fnameerr);
        awesomeValidation.addValidation(SpecialistRegi.this, R.id.lname, "[a-zA-Z\\s]+", R.string.lnameerr);
        //Validation of email
        awesomeValidation.addValidation(SpecialistRegi.this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerr);
        awesomeValidation.addValidation(SpecialistRegi.this, R.id.phoneNo, RegexTemplate.TELEPHONE, R.string.phoneerr);
        awesomeValidation.addValidation(SpecialistRegi.this, R.id.password, regexPassword, R.string.passerr);
        awesomeValidation.addValidation(SpecialistRegi.this, R.id.confPassword, R.id.password, R.string.cnfpasserr);

        //Set onClickListener of the login-button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Authenticating variables
                final String cFName = fname.getText().toString();
                final String cLName = lname.getText().toString();
                final String cEmail = email.getText().toString();
                final String cPhone = phone.getText().toString();
                final String cPassword = password.getText().toString();

                if(awesomeValidation.validate()){
                    mAuth.createUserWithEmailAndPassword(cEmail, cPassword)
                            .addOnCompleteListener(SpecialistRegi.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SpecialistRegi.this, ("Registration Failure! Try Again..."), Toast.LENGTH_SHORT).show();
                            }else{
                                //If successful print
                                Toast.makeText(SpecialistRegi.this, "Registration Success!", Toast.LENGTH_SHORT).show();
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String userid = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users").child("Specialists").child(userid);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("firstname", cFName );
                                hashMap.put("lastname", cLName);
                                hashMap.put("email", cEmail);
                                hashMap.put("phone", cPhone);

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //Move to next frame
                                            Intent intent = new Intent(SpecialistRegi.this, SpecialistLogin.class);
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
                    Toast.makeText(SpecialistRegi.this, "O Ja ERROR MR!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set onClickListener of the register
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialistRegi.this, SpecialistLogin.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
