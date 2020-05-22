package com.example.specialistfinderapp.Genysis;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.specialistfinderapp.Interface.IDialogClickListener;
import com.example.specialistfinderapp.R;
import com.google.android.material.textfield.TextInputEditText;

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

    }
}
