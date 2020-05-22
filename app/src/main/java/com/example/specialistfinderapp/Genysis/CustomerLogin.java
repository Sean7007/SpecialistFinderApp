package com.example.specialistfinderapp.Genysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.specialistfinderapp.MainActivity;
import com.example.specialistfinderapp.R;
import com.example.specialistfinderapp.User;
import com.example.specialistfinderapp.repository.UserImpl;
import com.example.specialistfinderapp.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class CustomerLogin extends AppCompatActivity  {
    //Variable Declaration
    Button login, back;
    private android.widget.EditText email, password, phoneNumber;
    AppCompatCheckBox checkBox;
    TextView register;
    TextView forgot;
    AwesomeValidation awesomeValidation;
    FirebaseUser firebaseUser;//Firebase User
    FirebaseAuth mAuth; //declaration of firebase
    private FirebaseAuth.AuthStateListener firebaseAuthListener; //State Listener of FirebaseAuth
    //Fire-store
    CollectionReference userRef1;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        //setContentView(R.layout.activity_login2);
        //Fire-Store
        userRef1 = FirebaseFirestore.getInstance().collection("Users");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        //=====================================CLOUD CODE===========================================//
        //Check if user exists
        if (getIntent() != null)
        {
            boolean  isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);
            if(isLogin)
            {

                dialog.show();
                //Check if User Exists
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Log.d(TAG, "onCreate: " + user.getPhoneNumber());

                //Save UserPhone by Paper
                Paper.init(CustomerLogin.this);
                Paper.book().write(Common.LOGGED_KEY, user.getDisplayName());

                DocumentReference currentUser = userRef1.document(user.getDisplayName());
                currentUser.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot userSnapShot = task.getResult();
                                    if(!userSnapShot.exists())
                                    {
                                        showUpdateDialog();
                                        Intent intent = new Intent(CustomerLogin.this, Customer_Home.class);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                    else
                                    {
                                        Common.currentUser = userSnapShot.toObject(User.class);
                                    }

                                    if(dialog.isShowing())
                                        dialog.dismiss();

                                }
                            }
                        });
            }

        }
        //=====================================END OF CLOUD CODE====================================//


        mAuth = FirebaseAuth.getInstance(); //Retrieves state of Fire-base
        login= findViewById(R.id.login);
        back = findViewById(R.id.back);
        checkBox = findViewById(R.id.checkbox);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);
        register= findViewById(R.id.registrationTextView);
        forgot= findViewById(R.id.forgotPasswordTextView);
        phoneNumber =  findViewById(R.id.phoneNo); //Just added

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
                String cEmail = email.getText().toString();
                String cPassword = password.getText().toString();

                //If validations are met-begin data authentication
                if(awesomeValidation.validate()){
                //Authenticating section
                mAuth.signInWithEmailAndPassword(cEmail, cPassword)
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
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

    }//End of onCreate

    //==========================================firebase Cloud code=======================================//
    private void showUpdateDialog() {
        if(dialog.isShowing())
            dialog.dismiss();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cEmail = email.getText().toString();
                String cPassword = password.getText().toString();
                User user = new User(cEmail,cPassword);
                userRef1.document(cEmail)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CustomerLogin.this, "RE KGONNE!", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Toast.makeText(CustomerLogin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

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

/*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        AppCompatTextView forgotPasswordBtn = (AppCompatTextView)findViewById(R.id.forgot);
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerLogin.this, "Todo - Forgot password implementation", Toast.LENGTH_SHORT).show();
            }
        });

        AppCompatTextView signUpBtn = (AppCompatTextView)findViewById(R.id.signup_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerLogin.this, "Todo - Sign implementation", Toast.LENGTH_SHORT).show();
                Intent signUpIntent = new Intent(CustomerLogin.this, CustomerRegi2.class);
                startActivity(signUpIntent);
            }
        });

        email = (AppCompatEditText)findViewById(R.id.email);
        passwordB = (AppCompatEditText)findViewById(R.id.password);

        AppCompatButton submitBtn = (AppCompatButton)findViewById(R.id.login_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = Objects.requireNonNull(email.getText()).toString();
                Log.d(TAG, "Log email " + emailAddress);
                String password = Objects.requireNonNull(passwordB.getText()).toString();

                if(TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
                    Toast.makeText(CustomerLogin.this, "Login Fields must not be empty", Toast.LENGTH_SHORT).show();
                }else if(!emailAddress.contains("@")){
                    Toast.makeText(CustomerLogin.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }else{
                    UserImpl userImplementation = new UserImpl(CustomerLogin.this);
                    userImplementation.getLoginUserByEmail(emailAddress);
                }
            }
        });*/
