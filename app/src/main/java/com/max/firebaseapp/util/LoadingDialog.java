package com.max.firebaseapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;
    private int layoutId;
    public LoadingDialog(Activity activity,int layoutId){

        //Layout -> R.Layout.custom_dialog.xml
        this.activity = activity;
        this.layoutId = layoutId;
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        //setando o layout do AlertDialog
        builder.setView(inflater.inflate(layoutId,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}
