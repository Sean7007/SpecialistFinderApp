package com.example.specialistfinderapp.Genysis;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.specialistfinderapp.Interface.IDialogClickListener;
import com.example.specialistfinderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialistCustomLoginDialog {

    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.edt_user)
    TextInputEditText edt_user;
    @BindView(R.id.edt_password)
    TextInputEditText edt_password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.back)
    Button back;

    FirebaseAuth mAuth; //declaration of fire-base


    public static SpecialistCustomLoginDialog mDialog;
    public IDialogClickListener iDialogClickListener;

    public static SpecialistCustomLoginDialog getInstance(){
        if(mDialog == null)
            mDialog = new SpecialistCustomLoginDialog();
        return mDialog;
    }
    public void showLoginDialog(String title,
                                String positiveText,
                                String negativeText,
                                Context context,
                                IDialogClickListener iDialogClickListener)
    {
        this.iDialogClickListener = iDialogClickListener;
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_specialist_login);

        ButterKnife.bind(this,dialog);
        //Set Title
        if(!TextUtils.isEmpty(title)) {
            txt_title.setText(title);
            txt_title.setVisibility(View.VISIBLE);
        }
        login.setText(positiveText);
        back.setText(negativeText);

        dialog.setCancelable(false);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDialogClickListener.onClickPositiveButton(dialog, edt_user.getText().toString(),edt_password.getText().toString());
            }
        });
         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 iDialogClickListener.onClickNegativeButton(dialog);
             }
         });

        //when the enter key is pressed
        edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_NULL) {

                    //step12
                    attemptLogIn();
                    return true;
                }
                return false;
            }
        });

    }

    private void attemptLogIn() {
        String emailV = edt_user.getText().toString();
        String passwordV = edt_password.getText().toString();

        if (emailV.equals("") || passwordV.equals("")) {
            //we dont continue to log in
            return;
        }
        else{
            //Toast.makeText(getApplicationContext(),"Login in Progress...",Toast.LENGTH_SHORT).show();

            //returns a TAsk Object, we are adding the addOnCompleteListener so we can see if the user has been signed in
            mAuth.signInWithEmailAndPassword(emailV, passwordV).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("whatsaa", "witnInWithEmail() onComplete: " + task.isSuccessful());

                    //this is triggered when the task is not successful
                    if (!task.isSuccessful()) {
                        Log.d("whatsaa", "Problem signing in:  " + task.getException());

                        //step14

                        showErrorDialog("There was a problem signing in!");

                    }
                    else{
                       // Intent intent = new Intent(SpecialistCustomLoginDialog.this, SpecialistHome2.class);
                       // finish();
                       // startActivity(intent);
                    }

                }

            });

        }

    }

    private void showErrorDialog(String message) {
       /* new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/

    }
}
