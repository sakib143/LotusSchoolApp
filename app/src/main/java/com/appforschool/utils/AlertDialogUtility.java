package com.appforschool.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.appforschool.R;


public class AlertDialogUtility {

    public static void showInternetAlert(Context context) {
        new AlertDialog.Builder(context).setIcon(0).setTitle(Constant.CHECK_INTERNET).setMessage(Constant.CHECK_INTERNET)
                .setCancelable(true).setNeutralButton("OK", null).show();
    }

    public static void CustomAlert(Context context, String title, String message, String Positive_text,
                                   String Negative_text,
                                   DialogInterface.OnClickListener PositiveListener,
                                   DialogInterface.OnClickListener NegativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(Positive_text, PositiveListener).setNegativeButton(Negative_text, NegativeListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showConfirmAlert(Context context, String msg, DialogInterface.OnClickListener onYesClick) {
        new AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.app_name)).setMessage(msg).setCancelable(true).setNegativeButton("NO", null)
                .setPositiveButton("YES", onYesClick).show();
    }

    public static void showSingleAlert(Context context, String strMessege, DialogInterface.OnClickListener onYesClick) {
        new AlertDialog.Builder(context).setIcon(0).setMessage(strMessege)
                .setTitle(context.getString(R.string.app_name))
                .setCancelable(false).setPositiveButton("OK", onYesClick)
                .show()
                .getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(context.getResources().getColor(R.color.colorHomeHeader));
    }

}
