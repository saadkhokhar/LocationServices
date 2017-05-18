package me.saadasad.locationservicesexample.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

/**
 * Created by Saad on 5/16/2017.
 */

public class AlertDialog {

    public static void CreateAlertDialog(Context context, String title, String message, String negativeButtonText, DialogInterface.OnClickListener negativeButtonClickListener, String positiveButtonText, DialogInterface.OnClickListener positiveButtonClickListener) {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(context);
        }
        if (title != null && title.equals("")) {
            builder.setTitle(title);
        }
        builder.setMessage(message)
                .setNegativeButton(negativeButtonText, negativeButtonClickListener)
                .setPositiveButton(positiveButtonText, positiveButtonClickListener)
                .show();
    }
}
